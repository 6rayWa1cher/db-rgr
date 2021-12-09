package com.a6raywa1cher.db_rgr.dblib;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ClassData {
	private final Map<String, FieldData> fieldDataMap;
	private final List<FieldData> fieldDataList;
	private final List<FieldData> primaryKey;
	private final List<FieldData> foreignKey;
	private final String tableName;

	public ClassData(List<FieldData> fieldDataList, String tableName) {
		this.fieldDataList = fieldDataList;
		this.fieldDataMap = fieldDataList.stream()
			.collect(Collectors.toMap(FieldData::fieldName, f -> f));
		this.primaryKey = fieldDataList.stream()
			.filter(FieldData::primary)
			.collect(Collectors.toList());
		this.foreignKey = fieldDataList.stream()
			.filter(fd -> fd.foreignKeyInfo().size() > 0)
			.collect(Collectors.toList());
		this.tableName = tableName;
	}

	public FieldData getByName(String fieldName) {
		return fieldDataMap.get(fieldName);
	}
}
