package com.a6raywa1cher.db_rgr.dblib;

import com.a6raywa1cher.db_rgr.lib.ArrayUtils;
import lombok.SneakyThrows;
import org.intellij.lang.annotations.Language;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.a6raywa1cher.db_rgr.lib.ReflectionUtils.wrapSneaky;

public abstract class CrudRepository<T extends Entity> {
	protected final Class<T> entityClass;

	protected final DatabaseConnector connector;

	protected final HashMap<UUID, Object[]> prevPrimaryKeys = new HashMap<>();

	protected ClassData classData;

	protected String fields;

	protected String defaultParametersPlaceholder;

	protected String primaryFields;

	protected String primaryParametersPlaceholder;

	protected String tableName;

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

	private String unsafeInjectParameters(@Language("SQL") String sql, Object... params) {
		return String.format(sql, params);
	}

	private String getParametersPlaceholder(int count) {
		return Stream.generate(() -> "?")
			.limit(count)
			.collect(Collectors.joining(","));
	}

	protected void registerObject(T t) {
		prevPrimaryKeys.put(t.getUuid(), classData.getPrimaryKey()
			.stream()
			.map(wrapSneaky(fd -> fd.getter().invoke(t)))
			.toArray());
	}

	protected void deregisterObject(T t) {
		prevPrimaryKeys.remove(t.getUuid());
	}

	@SneakyThrows
	public List<T> getAll() {
		List<T> out = connector.executeSelect(
			unsafeInjectParameters("SELECT * FROM public.%s", classData.getTableName()),
			entityClass
		);
		out.forEach(this::registerObject);
		return out;
	}

	@SneakyThrows
	public T getById(Object... id) {
		T out = connector.executeSelectSingle(
			unsafeInjectParameters("SELECT * from public.%s WHERE (%s) = (%s)",
				tableName,
				primaryFields,
				primaryParametersPlaceholder
			), entityClass, id
		);
		registerObject(out);
		return out;
	}

	@SneakyThrows
	public T insert(T t) {
		connector.executeUpdate(
			unsafeInjectParameters("INSERT INTO public.%s (%s) VALUES (%s)",
				tableName,
				fields,
				defaultParametersPlaceholder
			), objectToParameters(t)
		);
		registerObject(t);
		return t;
	}

	@SneakyThrows
	public long count() {
		return connector.executeSelectSingle(
			unsafeInjectParameters("SELECT count(*) from public.%s", tableName), Long.class
		);
	}

	@SneakyThrows
	public void update(T t) {
		Object[] prevPrimaryKey = prevPrimaryKeys.computeIfAbsent(t.getUuid(), u -> objectToPrimaryParameters(t));
		connector.executeUpdate(
			unsafeInjectParameters("UPDATE public.%s SET (%s) = (%s) WHERE (%s) = (%s)",
				tableName,
				fields,
				defaultParametersPlaceholder,
				primaryFields,
				primaryParametersPlaceholder
			), ArrayUtils.concat(objectToParameters(t), prevPrimaryKey)
		);
		registerObject(t);
	}

	@SneakyThrows
	public void delete(T t) {
		connector.executeUpdate(
			unsafeInjectParameters("DELETE FROM public.%s WHERE (%s) = (%s)",
				tableName,
				primaryFields,
				primaryParametersPlaceholder
			), objectToPrimaryParameters(t)
		);
		deregisterObject(t);
	}
}
