package com.a6raywa1cher.db_rgr.dblib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.a6raywa1cher.db_rgr.lib.StringUtils.camelCaseToUnderscore;
import static com.a6raywa1cher.db_rgr.lib.StringUtils.capitalizeFirstLetter;

public class ClassAnalyzer {
	private final static ClassAnalyzer classAnalyzer = new ClassAnalyzer();

	private final Map<Class<?>, ClassData> cachedFieldData = new HashMap<>();

	private ClassAnalyzer() {

	}

	public static ClassAnalyzer getInstance() {
		return classAnalyzer;
	}

	private String getGetterName(Field field) {
		String fieldName = field.getName();
		if (Boolean.class.isAssignableFrom(field.getType())) {
			return "is" + capitalizeFirstLetter(fieldName);
		} else {
			return "get" + capitalizeFirstLetter(fieldName);
		}
	}

	private String getSetterName(Field field) {
		String fieldName = field.getName();
		return "set" + capitalizeFirstLetter(fieldName);
	}

	private ClassData $getClassData(Class<?> clazz) {
		List<FieldData> fieldData = Stream.of(clazz.getDeclaredFields())
			.map(f -> {
				try {
					Column annotation = f.getAnnotation(Column.class);
					ForeignKey[] foreignKeys = f.getAnnotationsByType(ForeignKey.class);
					Class<?> fieldType = f.getType();
					Method getter = clazz.getMethod(getGetterName(f));
					Method setter = clazz.getMethod(getSetterName(f), fieldType);
					boolean pk = annotation.pk();
					String dbFieldName = annotation.value().equals("") ?
						camelCaseToUnderscore(f.getName()) :
						annotation.value();
					return new FieldData(
						dbFieldName,
						fieldType,
						getter,
						setter,
						pk,
						List.of(foreignKeys)
					);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				}
			})
			.collect(Collectors.toList());
		String tableName = clazz.getAnnotation(Entity.class).value();
		return new ClassData(
			fieldData,
			tableName.equals("") ? camelCaseToUnderscore(clazz.getSimpleName()) : tableName
		);
	}

	public ClassData getClassData(Class<?> clazz) {
		cachedFieldData.computeIfAbsent(clazz, this::$getClassData);
		return cachedFieldData.get(clazz);
	}
}
