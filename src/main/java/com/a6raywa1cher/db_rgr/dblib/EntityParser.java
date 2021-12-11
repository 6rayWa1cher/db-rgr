package com.a6raywa1cher.db_rgr.dblib;

import com.a6raywa1cher.db_rgr.dblib.analyzer.ClassAnalyzer;
import com.a6raywa1cher.db_rgr.dblib.analyzer.ClassData;
import com.a6raywa1cher.db_rgr.dblib.analyzer.FieldData;
import com.a6raywa1cher.db_rgr.dblib.datatypes.LocalDateDataType;
import com.a6raywa1cher.db_rgr.dblib.entity.Entity;
import com.a6raywa1cher.db_rgr.lib.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityParser {
	private final List<DataType> dataTypes = new ArrayList<>();
	private final ClassAnalyzer classAnalyzer;

	public EntityParser(ClassAnalyzer classAnalyzer) {
		this.classAnalyzer = classAnalyzer;
		addDefaultDataTypes();
	}

	private void addDefaultDataTypes() {
		dataTypes.add(new LocalDateDataType());
	}

	public void registerDataType(DataType<?, ?> dataType) {
		dataTypes.add(dataType);
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> parseResultEntity(Class<T> resultType, ResultSetMetaData metaData, List<Object[]> objects) {
		List<T> out = new ArrayList<>(objects.size());
		ClassData fieldDataOfClass = classAnalyzer.getClassData((Class<? extends Entity>) resultType);
		try {
			for (Object[] obj : objects) {
				T t = ReflectionUtils.instantiate(resultType);
				for (int i = 0; i < obj.length; i++) {
					Object o = obj[i];
					if (o == null) continue;
					String fieldName = metaData.getColumnName(i + 1);
					FieldData fieldData = fieldDataOfClass.getByName(fieldName);
					Objects.requireNonNull(fieldData);
					DataType eligibleDataType = dataTypes.stream()
						.filter(dt -> dt.getSerializedType().equals(o.getClass()) &&
							dt.getDeserializedType().equals(fieldData.type()))
						.findFirst()
						.orElse(null);
					if (eligibleDataType != null) {
						fieldData.setter().invoke(t,
							eligibleDataType.deserialize(eligibleDataType.getSerializedType().cast(o))
						);
					} else {
						fieldData.setter().invoke(t, fieldData.type().cast(o));
					}
				}
				out.add(t);
			}
			return out;
		} catch (IllegalAccessException | InvocationTargetException | SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private <T> List<T> parseResultPrimitive(Class<T> resultType, List<Object[]> objects) {
		List<T> out = new ArrayList<>(objects.size());
		for (Object[] obj : objects) {
			out.add(resultType.cast(obj[0]));
		}
		return out;
	}

	public <T> List<T> parseResult(Class<T> resultType, ExecuteResult executeResult) {
		if (Entity.class.isAssignableFrom(resultType)) {
			return parseResultEntity(resultType, executeResult.metaData(), executeResult.result());
		} else {
			return parseResultPrimitive(resultType, executeResult.result());
		}
	}
}
