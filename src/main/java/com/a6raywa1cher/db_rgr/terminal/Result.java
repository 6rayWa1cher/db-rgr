package com.a6raywa1cher.db_rgr.terminal;

import java.util.Collections;
import java.util.Set;

public record Result(
	Class<? extends Controller> nextController,
	String nextMethod,
	Set<Object> newBag
) {
	public Result(Class<? extends Controller> nextController, String nextMethod) {
		this(nextController, nextMethod, Collections.emptySet());
	}
}
