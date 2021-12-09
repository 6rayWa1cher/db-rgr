package com.a6raywa1cher.db_rgr.dblib;

import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatabaseConnector implements AutoCloseable {
    private final Connection con;
    private final ClassAnalyzer classAnalyzer = ClassAnalyzer.getInstance();

    @SneakyThrows(ClassNotFoundException.class)
    public DatabaseConnector(String jdbcUrl, String user, String password) throws SQLException {
        instantiate(Class.forName("org.postgresql.Driver"));
        this.con = DriverManager.getConnection(jdbcUrl, user, password);
        con.setAutoCommit(false);
    }

    public PreparedStatement openPS(String sql, Object... params) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
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

    public <T> List<T> executeSelect(String sql, Class<T> resultType, Object... params) throws SQLException {
        try (PreparedStatement preparedStatement = openPS(sql, params)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<T> out = new ArrayList<>();
            if (resultType.isAnnotationPresent(Entity.class)) {
                Map<String, FieldData> fieldDataMap = classAnalyzer.getFieldDataOfClass(resultType);
                while (resultSet.next()) {
                    T obj = instantiate(resultType);
                    for (int i = 0; i < metaData.getColumnCount(); i++) {
                        FieldData fieldData = fieldDataMap.get(metaData.getColumnLabel(i));
                        Class<?> type = fieldData.type();
                        Object o = resultSet.getObject(i + 1);
                        fieldData.setter().invoke(obj, type.cast(o));
                    }
                    out.add(obj);
                }
            } else {
                while (resultSet.next()) {
                    Object o = resultSet.getObject(1);
                    out.add(resultType.cast(o));
                }
            }
            return out;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T executeSelectSingle(String sql, Class<T> resultType, Object... params) throws SQLException {
        return executeSelect(sql, resultType, params).stream().findFirst().orElse(null);
    }

    @Override
    public void close() throws SQLException {
        con.close();
    }
}
