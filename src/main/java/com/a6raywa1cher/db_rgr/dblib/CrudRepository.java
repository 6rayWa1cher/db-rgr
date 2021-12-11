package com.a6raywa1cher.db_rgr.dblib;

import com.a6raywa1cher.db_rgr.dblib.analyzer.ClassData;
import com.a6raywa1cher.db_rgr.dblib.analyzer.FieldData;
import com.a6raywa1cher.db_rgr.dblib.entity.Entity;
import com.a6raywa1cher.db_rgr.lib.ArrayUtils;
import lombok.SneakyThrows;

import java.util.List;
import java.util.stream.Collectors;

import static com.a6raywa1cher.db_rgr.lib.ReflectionUtils.wrapSneaky;
import static com.a6raywa1cher.db_rgr.lib.SqlUtils.getParametersPlaceholder;
import static com.a6raywa1cher.db_rgr.lib.SqlUtils.unsafeInjectParameters;

public abstract class CrudRepository<T extends Entity> {
	protected final Class<T> entityClass;

	protected final EntityManager entityManager;

	protected ClassData classData;

	protected String fields;

	protected String defaultParametersPlaceholder;

	protected String primaryFields;

	protected String primaryParametersPlaceholder;

	protected String tableName;

	public CrudRepository(Class<T> entityClass, EntityManager entityManager) {
		this.entityClass = entityClass;
		this.entityManager = entityManager;
		initialize();
	}

	private void initialize() {
		this.classData = entityManager.getClassAnalyzer().getClassData(entityClass);
		this.fields = toFieldList(classData.getFieldDataList());
		this.tableName = classData.getTableName();
		this.defaultParametersPlaceholder = getParametersPlaceholder(classData.getFieldDataList().size());
		this.primaryFields = toFieldList(classData.getPrimaryKey());
		this.primaryParametersPlaceholder = getParametersPlaceholder(classData.getPrimaryKey().size());
	}

	private String toFieldList(List<FieldData> fieldData) {
		return fieldData.stream()
			.map(FieldData::fieldName)
			.collect(Collectors.joining(","));
	}

	private Object[] objectToParameters(T t) {
		return classData.getFieldDataList()
			.stream()
			.map(wrapSneaky(fd -> fd.getter().invoke(t)))
			.toArray();
	}

	private Object[] objectToPrimaryParameters(T t) {
		return classData.getPrimaryKey()
			.stream()
			.map(wrapSneaky(fd -> fd.getter().invoke(t)))
			.toArray();
	}

	@SneakyThrows
	public List<T> getAll() {
		return entityManager.executeSelect(
			unsafeInjectParameters("SELECT * FROM public.%s", classData.getTableName()),
			entityClass
		);
	}

	@SneakyThrows
	public T getById(Object... id) {
		return entityManager.executeSelectSingle(
			unsafeInjectParameters("SELECT * from public.%s WHERE (%s) = (%s)",
				tableName,
				primaryFields,
				primaryParametersPlaceholder
			), entityClass, id
		);
	}

	@SneakyThrows
	public T insert(T t) {
		entityManager.executeUpdate(
			unsafeInjectParameters("INSERT INTO public.%s (%s) VALUES (%s)",
				tableName,
				fields,
				defaultParametersPlaceholder
			), objectToParameters(t)
		);
		entityManager.updateEntityInfo(t);
		return t;
	}

	@SneakyThrows
	public long count() {
		return entityManager.executeSelectSingle(
			unsafeInjectParameters("SELECT count(*) from public.%s", tableName), Long.class
		);
	}

	@SneakyThrows
	public void update(T t) {
		Object[] prevPrimaryKey = entityManager.getRecordedPrimaryKey(t);
		entityManager.executeUpdate(
			unsafeInjectParameters("UPDATE public.%s SET (%s) = (%s) WHERE (%s) = (%s)",
				tableName,
				fields,
				defaultParametersPlaceholder,
				primaryFields,
				primaryParametersPlaceholder
			), ArrayUtils.concat(objectToParameters(t), prevPrimaryKey)
		);
		entityManager.updateEntityInfo(t);
	}

	@SneakyThrows
	public void delete(T t) {
		entityManager.executeUpdate(
			unsafeInjectParameters("DELETE FROM public.%s WHERE (%s) = (%s)",
				tableName,
				primaryFields,
				primaryParametersPlaceholder
			), objectToPrimaryParameters(t)
		);
		entityManager.deregisterObject(t);
	}
}
