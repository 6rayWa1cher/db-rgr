package com.a6raywa1cher.db_rgr;

import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import lombok.SneakyThrows;

public class CommonSqlQueries {
	private final EntityManager em;

	public CommonSqlQueries(EntityManager em) {
		this.em = em;
	}

	@SneakyThrows
	public String getDatabaseName() {
		return em.executeSelectSingle("SELECT current_database()", String.class);
	}
}
