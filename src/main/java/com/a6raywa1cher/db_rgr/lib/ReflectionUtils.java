package com.a6raywa1cher.db_rgr.lib;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.a6raywa1cher.db_rgr.lib.StringUtils.capitalizeFirstLetter;

public final class ReflectionUtils {
    private static String getGetterName(Field field) {
        String fieldName = field.getName();
        if (Boolean.class.isAssignableFrom(field.getType())) {
            return "is" + capitalizeFirstLetter(fieldName);
        } else {
            return "get" + capitalizeFirstLetter(fieldName);
        }
    }

    private static String getSetterName(Field field) {
        String fieldName = field.getName();
        return "get" + capitalizeFirstLetter(fieldName);
    }

    public static List<FieldData> getFieldDataOfClass(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                .map(f -> {
                    try {
                        return new FieldData(
                                f.getAnnotation(Column.class).value(),
                                clazz.getMethod(getGetterName(f)),
                                clazz.getMethod(getSetterName(f), f.getType()),
                                f.getAnnotation(Column.class).pk(),
                                f.getAnnotation(ForeignKey.class)
                        );
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
