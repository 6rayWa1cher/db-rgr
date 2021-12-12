package com.a6raywa1cher.db_rgr.terminal.parsers;

import com.a6raywa1cher.db_rgr.terminal.DataParser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LocalDateDataParser implements DataParser<LocalDate> {
	@Override
	public LocalDate parse(String input) {
		try {
			return LocalDate.parse(input);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public Class<LocalDate> getTarget() {
		return LocalDate.class;
	}
}
