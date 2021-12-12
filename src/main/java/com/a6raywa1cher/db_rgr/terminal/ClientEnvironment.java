package com.a6raywa1cher.db_rgr.terminal;

import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import lombok.Data;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Set;

@Data
public final class ClientEnvironment {
	private final Scanner scanner;
	private final PrintWriter printWriter;
	private final EntityManager entityManager;
	private final StringParser stringParser;
	private Set<Object> bag;

	public ClientEnvironment(
		Scanner scanner,
		PrintWriter printWriter,
		EntityManager entityManager,
		StringParser stringParser, Set<Object> bag
	) {
		this.scanner = scanner;
		this.printWriter = printWriter;
		this.entityManager = entityManager;
		this.stringParser = stringParser;
		this.bag = bag;
	}
}
