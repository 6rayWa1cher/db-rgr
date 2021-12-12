package com.a6raywa1cher.db_rgr.terminal;

import com.a6raywa1cher.db_rgr.terminal.parsers.IntegerDataParser;
import com.a6raywa1cher.db_rgr.terminal.parsers.LocalDateDataParser;

import java.util.HashMap;
import java.util.Map;

public class StringParser {
	private final Map<Class<?>, DataParser<?>> map = new HashMap<>();

	public StringParser() {

	}

	private void addDefaultParsers() {
		register(new LocalDateDataParser());
		register(new IntegerDataParser());
	}

	public <T> void register(DataParser<T> dataParser) {
		map.put(dataParser.getTarget(), dataParser);
	}

	public <T> T parse(String string, Class<T> targetClass) throws IllegalArgumentException {
		if (String.class.isAssignableFrom(targetClass)) {
			return (T) string;
		}
		DataParser<T> dataParser = (DataParser<T>) map.get(targetClass);
		return dataParser.parse(string);
	}
}
