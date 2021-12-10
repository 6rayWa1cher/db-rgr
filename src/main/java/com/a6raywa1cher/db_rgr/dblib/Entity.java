package com.a6raywa1cher.db_rgr.dblib;

import java.util.UUID;

public abstract class Entity {
	private final UUID uuid = UUID.randomUUID();

	public UUID getUuid() {
		return uuid;
	}

	public String getTableName() {
		return "";
	}
}
