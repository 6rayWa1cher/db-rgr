package com.a6raywa1cher.db_rgr.dblib;

import com.a6raywa1cher.db_rgr.lib.ReflectionUtils;
import lombok.SneakyThrows;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector implements AutoCloseable {
	private final Connection con;
	private boolean prevAutoCommit = true;

	@SneakyThrows(ClassNotFoundException.class)
	DatabaseConnector(String jdbcUrl, String user, String password) throws SQLException {
		ReflectionUtils.instantiate(Class.forName("org.postgresql.Driver"));
		this.con = DriverManager.getConnection(jdbcUrl, user, password);
		con.setAutoCommit(prevAutoCommit);
	}


	public PreparedStatement openPS(@Language("SQL") String sql, Object... params) throws SQLException {
		PreparedStatement ps = con.prepareStatement(sql);
		for (int i = 0; i < params.length; i++) {
			Object param = params[i];
			ps.setObject(i + 1, param);
		}
		return ps;
	}

	public ExecuteResult executeSelect(@Language("SQL") String sql, Object... params) throws SQLException {
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

	public void beginTransaction(int level) throws SQLException {
		prevAutoCommit = con.getAutoCommit();
		con.setAutoCommit(false);
		con.setTransactionIsolation(level);
	}

	public void commit() throws SQLException {
		con.commit();
		con.setAutoCommit(prevAutoCommit);
	}

	public void rollback() throws SQLException {
		con.rollback();
		con.setAutoCommit(prevAutoCommit);
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

}
