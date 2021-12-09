package com.a6raywa1cher.db_rgr.dblib;

import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CrudRepository<T> {
	private final Class<T> entityClass;

	private final DatabaseConnector connector;

	private Map<String, FieldData> fieldDataMap;

	private List<FieldData> primaryKey;

	private String tableName;

	public CrudRepository(Class<T> entityClass, DatabaseConnector connector) {
		this.entityClass = entityClass;
		this.connector = connector;
		initialize();
	}

	private void initialize() {
		this.fieldDataMap = ClassAnalyzer.getInstance().getFieldDataOfClass(entityClass);
		this.primaryKey = fieldDataMap.values()
			.stream()
			.filter(FieldData::primary)
			.collect(Collectors.toList());
		this.tableName = entityClass.getAnnotation(Entity.class).value();
	}

	@SneakyThrows
	public List<T> getAll() {
		return connector.executeSelect(String.format("SELECT * FROM public.%s", tableName), entityClass);
	}

	public List<T> getById(Object... id) {
		return null;
	}
}
