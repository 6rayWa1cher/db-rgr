package com.a6raywa1cher.db_rgr.lib;

import java.lang.reflect.Method;

public record FieldData(
        String fieldName,
        Method getter,
        Method setter,
        boolean primary,
        ForeignKey foreignKeyInfo
) {

}
