package com.a6raywa1cher.db_rgr.dblib;

import lombok.SneakyThrows;
import org.intellij.lang.annotations.Language;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CrudRepository<T> {
	private final Class<T> entityClass;

	private final DatabaseConnector connector;

	private ClassData classData;

	private String fields;

	private String defaultParametersPlaceholder;

	private String primaryFields;

	private String primaryParametersPlaceholder;

	private String tableName;

	public CrudRepository(Class<T> entityClass, DatabaseConnector connector) {
		this.entityClass = entityClass;
		this.connector = connector;
		initialize();
	}

	private void initialize() {
		this.classData = ClassAnalyzer.getInstance().getClassData(entityClass);
		this.fields = toFieldList(classData.getFieldDataList());
		this.tableName = classData.getTableName();
		this.defaultParametersPlaceholder = getParametersPlaceholder(classData.getFieldDataList().size());
		this.primaryFields = toFieldList(classData.getPrimaryKey());
		this.primaryParametersPlaceholder = getParametersPlaceholder(classData.getPrimaryKey().size());
	}

	@SneakyThrows
	public List<T> getAll() {
		return connector.executeSelect(
			unsafeInjectParameters("SELECT * FROM public.%s", classData.getTableName()),
			entityClass
		);
	}

	private String toFieldList(List<FieldData> fieldData) {
		return fieldData.stream()
			.map(FieldData::fieldName)
			.collect(Collectors.joining(","));
	}

	private Object[] objectToParameters(T t) {
		return classData.getFieldDataList()
			.stream()
			.map(fd -> {
				try {
					return fd.getter().invoke(t);
				} catch (IllegalAccessException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			})
			.toArray();
	}

	private String unsafeInjectParameters(@Language("SQL") String sql, Object... params) {
		return String.format(sql, params);
	}

	private String getParametersPlaceholder(int count) {
		return Stream.generate(() -> "?")
			.limit(count)
			.collect(Collectors.joining(","));
	}

	@SneakyThrows
	public T getById(Object... id) {
		return connector.executeSelectSingle(
			unsafeInjectParameters("SELECT * from public.%s WHERE (%s) = (%s)",
				tableName,
				primaryFields,
				primaryParametersPlaceholder
			), entityClass, id
		);
	}

	@SneakyThrows
	public void insert(T t) {
		connector.executeUpdate(
			unsafeInjectParameters("INSERT INTO public.%s (%s) VALUES (%s)",
				tableName,
				fields,
				defaultParametersPlaceholder
			), objectToParameters(t)
		);
	}
}
