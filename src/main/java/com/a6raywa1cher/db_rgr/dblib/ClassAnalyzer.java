package com.a6raywa1cher.db_rgr.dblib;

import lombok.SneakyThrows;

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

	private final Map<Class<? extends Entity>, ClassData> cachedFieldData = new HashMap<>();

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

	@SneakyThrows
	private ClassData $getClassData(Class<? extends Entity> clazz) {
		List<FieldData> fieldData = Stream.of(clazz.getDeclaredFields())
			.filter(f -> f.isAnnotationPresent(Column.class))
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
		String tableName = clazz.getDeclaredConstructor().newInstance().getTableName();
		return new ClassData(
			fieldData,
			tableName.equals("") ? camelCaseToUnderscore(clazz.getSimpleName()) : tableName
		);
	}

	public ClassData getClassData(Class<? extends Entity> clazz) {
		cachedFieldData.computeIfAbsent(clazz, this::$getClassData);
		return cachedFieldData.get(clazz);
	}
}
