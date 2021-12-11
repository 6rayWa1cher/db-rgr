package com.a6raywa1cher.db_rgr;

import com.a6raywa1cher.db_rgr.dblib.DbLibInitializer;
import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.terminal.TerminalClient;

import static com.a6raywa1cher.db_rgr.SchemaInitializer.createTables;

public class Main {
	public static void main(String[] args) throws Exception {
		try (EntityManager em = DbLibInitializer.initialize()) {
			CommonSqlQueries commonSqlQueries = new CommonSqlQueries(em);
			System.out.println("Connected to database " + commonSqlQueries.getDatabaseName());
			createTables(em);

			try (TerminalClient client = new TerminalClient(em, System.in, System.out)) {
				client.start();
			}
		}
	}
}
