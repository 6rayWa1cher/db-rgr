package com.a6raywa1cher.db_rgr.terminal;

public interface DataParser<T> {
	T parse(String input) throws IllegalArgumentException;

	Class<T> getTarget();
}
