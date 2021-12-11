package com.a6raywa1cher.db_rgr;

import com.a6raywa1cher.db_rgr.dblib.DbLibInitializer;
import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

public class DatabaseInitializedTest {
	protected static EntityManager em;

	@BeforeAll
	static void connect() throws Exception {
		em = DbLibInitializer.initialize();
	}

	@AfterAll
	static void disconnect() throws Exception {
		em.close();
	}

	@BeforeEach
	void prepareDatabase() throws SQLException {
		Main.dropDatabase(em);
		Main.createTables(em);
	}
}
