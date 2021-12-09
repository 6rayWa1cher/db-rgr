package com.a6raywa1cher.db_rgr.dblib;

import java.lang.reflect.Method;
import java.util.List;

public record FieldData(
	String fieldName,
	Class<?> type,
	Method getter,
	Method setter,
	boolean primary,
	List<ForeignKey> foreignKeyInfo
) {

}
