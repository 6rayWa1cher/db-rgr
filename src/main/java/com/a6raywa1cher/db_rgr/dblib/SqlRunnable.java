package com.a6raywa1cher.db_rgr.dblib;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlRunnable {
	void run() throws SQLException;
}
