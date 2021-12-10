package com.a6raywa1cher.db_rgr.lib;

import java.util.Locale;

public final class StringUtils {
	public static String capitalizeFirstLetter(String s) {
		return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
	}

	public static String lowerFirstLetter(String s) {
		return s.substring(0, 1).toLowerCase(Locale.ROOT) + s.substring(1);
	}

	public static String camelCaseToUnderscore(String str) {
		if (str.length() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder("" + Character.toLowerCase(str.charAt(0)));
		for (int i = 1; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append('_').append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
