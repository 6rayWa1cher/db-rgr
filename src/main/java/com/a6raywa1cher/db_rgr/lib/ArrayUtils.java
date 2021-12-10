package com.a6raywa1cher.db_rgr.lib;

public final class ArrayUtils {
	public static Object[] concat(Object[] a, Object[] b) {
		Object[] c = new Object[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
}
