package com.a6raywa1cher.db_rgr.lib;

import java.util.function.Function;

public final class ReflectionUtils {
	public static <T, R> Function<T, R> wrapSneaky(FunctionWithException<T, R> function) {
		return (o) -> {
			try {
				return function.call(o);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	@FunctionalInterface
	public interface FunctionWithException<T, R> {
		R call(T t) throws Exception;
	}
}
