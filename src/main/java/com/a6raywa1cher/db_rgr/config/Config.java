package com.a6raywa1cher.db_rgr.config;

import lombok.Data;

@Data
public class Config {
	private Db db;

	@Data
	public static class Db {
		private String jdbc;

		private String user;

		private String password;
	}
}
