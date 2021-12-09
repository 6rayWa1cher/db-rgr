package com.a6raywa1cher.db_rgr.dblib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.a6raywa1cher.db_rgr.dblib.StringUtils.capitalizeFirstLetter;

public class ClassAnalyzer {
	private final static ClassAnalyzer classAnalyzer = new ClassAnalyzer();

	private final Map<Class<?>, Map<String, FieldData>> cachedFieldData = new HashMap<>();

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

	private Map<String, FieldData> $getFieldDataOfClass(Class<?> clazz) {
		return Stream.of(clazz.getDeclaredFields())
			.map(f -> {
				try {
					Column annotation = f.getAnnotation(Column.class);
					ForeignKey foreignKey = f.getAnnotation(ForeignKey.class);
					Class<?> fieldType = f.getType();
					Method getter = clazz.getMethod(getGetterName(f));
					Method setter = clazz.getMethod(getSetterName(f), fieldType);
					boolean pk = annotation.pk();
					String dbFieldName = annotation.value();
					return new FieldData(
						dbFieldName,
						fieldType,
						getter,
						setter,
						pk,
						foreignKey
					);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				}
			})
			.collect(Collectors.toMap(FieldData::fieldName, f -> f));
	}

	public Map<String, FieldData> getFieldDataOfClass(Class<?> clazz) {
		cachedFieldData.computeIfAbsent(clazz, this::$getFieldDataOfClass);
		return cachedFieldData.get(clazz);
	}
}
