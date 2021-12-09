package com.a6raywa1cher.db_rgr.dblib;

import java.lang.reflect.Field;
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
        return "get" + capitalizeFirstLetter(fieldName);
    }

    private Map<String, FieldData> $getFieldDataOfClass(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                .map(f -> {
                    try {
                        return new FieldData(
                                f.getAnnotation(Column.class).value(),
                                f.getType(),
                                clazz.getMethod(getGetterName(f)),
                                clazz.getMethod(getSetterName(f), f.getType()),
                                f.getAnnotation(Column.class).pk(),
                                f.getAnnotation(ForeignKey.class)
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
