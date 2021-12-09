package com.a6raywa1cher.db_rgr.lib;

import java.util.Locale;
import java.util.stream.IntStream;

public final class StringUtils {
	public static String capitalizeFirstLetter(String s) {
		return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
	}

	public static String lowerFirstLetter(String s) {
		return s.substring(0, 1).toLowerCase(Locale.ROOT) + s.substring(1);
	}

	public static String camelCaseToUnderscore(String str) {
		String s = lowerFirstLetter(str);
		int[] capitals = IntStream.range(0, s.length())
			.filter(i -> Character.isUpperCase(s.charAt(i)))
			.toArray();
		if (capitals.length == 0) {
			return s;
		}
		StringBuilder sb = new StringBuilder(s.length());
		int prev = 0;
		for (int curr : capitals) {
			sb.append(s.substring(prev, curr).toLowerCase(Locale.ROOT)).append("_");
			prev = curr;
		}
		sb.append(s.substring(prev).toLowerCase(Locale.ROOT));
		return sb.toString();
	}
}
