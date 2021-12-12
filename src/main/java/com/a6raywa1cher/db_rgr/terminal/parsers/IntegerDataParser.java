package com.a6raywa1cher.db_rgr.terminal.parsers;

import com.a6raywa1cher.db_rgr.terminal.DataParser;

public class IntegerDataParser implements DataParser<Integer> {
	@Override
	public Integer parse(String input) throws IllegalArgumentException {
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public Class<Integer> getTarget() {
		return Integer.class;
	}
}
