package com.a6raywa1cher.db_rgr.dblib;

import java.lang.reflect.Method;

public record FieldData(
        String fieldName,
        Class<?> type,
        Method getter,
        Method setter,
        boolean primary,
        ForeignKey foreignKeyInfo
) {

}
