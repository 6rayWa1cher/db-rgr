package com.a6raywa1cher.db_rgr.lib;

import org.intellij.lang.annotations.Language;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SqlUtils {
	public static String unsafeInjectParameters(@Language("SQL") String sql, Object... params) {
		return String.format(sql, params);
	}

	public static String getParametersPlaceholder(int count) {
		return Stream.generate(() -> "?")
			.limit(count)
			.collect(Collectors.joining(","));
	}
}
