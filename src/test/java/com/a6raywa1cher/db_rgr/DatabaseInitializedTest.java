package com.a6raywa1cher.db_rgr;

import com.a6raywa1cher.db_rgr.dblib.DatabaseConnector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

public class DatabaseInitializedTest {
	protected static DatabaseConnector connector;

	@BeforeAll
	static void connect() throws SQLException {
		connector = Main.initConnector();
	}

	@AfterAll
	static void disconnect() throws SQLException {
		connector.close();
	}

	@BeforeEach
	void prepareDatabase() throws SQLException {
		Main.dropDatabase(connector);
		Main.createTables(connector);
	}
}
