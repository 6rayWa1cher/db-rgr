package com.a6raywa1cher.db_rgr.dblib.entity;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(ForeignKeys.class)
public @interface ForeignKey {
	Class<?> targetClass();

	String targetField();
}
