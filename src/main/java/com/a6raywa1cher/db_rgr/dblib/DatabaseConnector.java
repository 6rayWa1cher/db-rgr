package com.a6raywa1cher.db_rgr.dblib;

import lombok.SneakyThrows;
import org.intellij.lang.annotations.Language;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

public class DatabaseConnector implements AutoCloseable {
    private final Connection con;
    private final ClassAnalyzer classAnalyzer = ClassAnalyzer.getInstance();
    private final Map<String, PreparedStatement> cachedPS = new HashMap<>();

    @SneakyThrows(ClassNotFoundException.class)
    public DatabaseConnector(String jdbcUrl, String user, String password) throws SQLException {
        instantiate(Class.forName("org.postgresql.Driver"));
        this.con = DriverManager.getConnection(jdbcUrl, user, password);
    }

    public PreparedStatement openPS(@Language("SQL") String sql, Object... params) throws SQLException {
        if (!cachedPS.containsKey(sql)) {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            cachedPS.put(sql, preparedStatement);
        }
        PreparedStatement ps = cachedPS.get(sql);
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            ps.setObject(i, param);
        }
        return ps;
    }

    @SneakyThrows
    private <T> T instantiate(Class<T> type) {
        return type.getDeclaredConstructor().newInstance();
    }

    public ExecuteResult executeRawSelect(@Language("SQL") String sql, Object... params) throws SQLException {
        try (PreparedStatement preparedStatement = openPS(sql, params)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<Object[]> out = new ArrayList<>();
            while (resultSet.next()) {
                Object[] obj = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    obj[i] = resultSet.getObject(i + 1);
                }
                out.add(obj);
            }
            return new ExecuteResult(metaData, out);
        }
    }

    private <T> List<T> parseResultEntity(Class<T> resultType, ResultSetMetaData metaData, List<Object[]> objects) throws SQLException {
        List<T> out = new ArrayList<>(objects.size());
        Map<String, FieldData> fieldDataOfClass = classAnalyzer.getFieldDataOfClass(resultType);
        try {
            for (Object[] obj : objects) {
                T t = instantiate(resultType);
                for (int i = 0; i < obj.length; i++) {
                    String fieldName = metaData.getColumnName(i + 1);
                    FieldData fieldData = fieldDataOfClass.get(fieldName);
                    Objects.requireNonNull(fieldData);
                    fieldData.setter().invoke(t, fieldData.type().cast(obj[i]));
                }
                out.add(t);
            }
            return out;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<T> parseResultPrimitive(Class<T> resultType, List<Object[]> objects) {
        List<T> out = new ArrayList<>(objects.size());
        for (Object[] obj : objects) {
            out.add(resultType.cast(obj[0]));
        }
        return out;
    }

    public <T> List<T> executeSelect(@Language("SQL") String sql, Class<T> resultType, Object... params) throws SQLException {
        ExecuteResult result = executeRawSelect(sql, params);
        ResultSetMetaData metaData = result.metaData();
        List<Object[]> objects = result.result();
        if (resultType.isAnnotationPresent(Entity.class)) {
            return parseResultEntity(resultType, metaData, objects);
        } else {
            return parseResultPrimitive(resultType, objects);
        }
    }

    public <T> T executeSelectSingle(@Language("SQL") String sql, Class<T> resultType, Object... params) throws SQLException {
        return executeSelect(sql, resultType, params).stream().findFirst().orElse(null);
    }

    public void execute(@Language("SQL") String sql, Object... params) throws SQLException {
		try (PreparedStatement preparedStatement = openPS(sql, params)) {
			preparedStatement.execute();
		}
	}

	public int executeUpdate(@Language("SQL") String sql, Object... params) throws SQLException {
		try (PreparedStatement preparedStatement = openPS(sql, params)) {
			return preparedStatement.executeUpdate();
		}
	}

	public void withTransaction(SqlRunnable runnable) throws SQLException {
		boolean autoCommit = con.getAutoCommit();
		con.setAutoCommit(false);
		try {
			runnable.run();
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.setAutoCommit(autoCommit);
		}
	}

	@Override
	public void close() throws SQLException {
		con.close();
	}

	public record ExecuteResult(
		ResultSetMetaData metaData,
		List<Object[]> result
	) {

    }
}
