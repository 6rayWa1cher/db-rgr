package com.a6raywa1cher.db_rgr.dblib;

public record SqlScript(
	String sql,
	Object[] params
) {
}
