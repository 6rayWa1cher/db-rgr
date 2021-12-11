package com.a6raywa1cher.db_rgr.terminal;

import com.a6raywa1cher.db_rgr.dblib.EntityManager;

import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public final class ClientEnvironment {
	private final Scanner scanner;
	private final PrintWriter printWriter;
	private final EntityManager entityManager;
	private Set<Object> bag;

	public ClientEnvironment(
		Scanner scanner,
		PrintWriter printWriter,
		EntityManager entityManager,
		Set<Object> bag
	) {
		this.scanner = scanner;
		this.printWriter = printWriter;
		this.entityManager = entityManager;
		this.bag = bag;
	}

	public Scanner getScanner() {
		return scanner;
	}

	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public Set<Object> getBag() {
		return bag;
	}

	public void setBag(Set<Object> bag) {
		this.bag = bag;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (ClientEnvironment) obj;
		return Objects.equals(this.scanner, that.scanner) &&
			Objects.equals(this.printWriter, that.printWriter) &&
			Objects.equals(this.entityManager, that.entityManager) &&
			Objects.equals(this.bag, that.bag);
	}

	@Override
	public int hashCode() {
		return Objects.hash(scanner, printWriter, entityManager, bag);
	}

	@Override
	public String toString() {
		return "ClientEnvironment[" +
			"scanner=" + scanner + ", " +
			"printWriter=" + printWriter + ", " +
			"entityManager=" + entityManager + ", " +
			"bag=" + bag + ']';
	}


}
