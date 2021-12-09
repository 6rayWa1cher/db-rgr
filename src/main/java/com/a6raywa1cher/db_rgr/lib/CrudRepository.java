package com.a6raywa1cher.db_rgr.lib;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CrudRepository<T> {
    private final Class<T> entityClass;

    private Map<String, FieldData> fieldDataMap;

    private List<FieldData> primaryKey;

    public CrudRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
        initialize();
    }

    private void initialize() {
        this.fieldDataMap = ReflectionUtils.getFieldDataOfClass(entityClass)
                .stream()
                .collect(Collectors.toMap(FieldData::fieldName, f -> f));
        this.primaryKey = fieldDataMap.values()
                .stream()
                .filter(FieldData::primary)
                .collect(Collectors.toList());
    }

    public List<T> getById(Object... id) {

    }
}
