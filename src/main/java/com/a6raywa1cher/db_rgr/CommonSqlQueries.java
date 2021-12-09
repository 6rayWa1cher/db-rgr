package com.a6raywa1cher.db_rgr;

import com.a6raywa1cher.db_rgr.dblib.DatabaseConnector;
import lombok.SneakyThrows;

public class CommonSqlQueries {
	private final DatabaseConnector connector;

	public CommonSqlQueries(DatabaseConnector connector) {
		this.connector = connector;
	}

	@SneakyThrows
	public String getDatabaseName() {
		return connector.executeSelectSingle("SELECT current_database()", String.class);
	}
}
