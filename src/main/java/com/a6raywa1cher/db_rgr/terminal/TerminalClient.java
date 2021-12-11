package com.a6raywa1cher.db_rgr.terminal;

import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.terminal.controller.DepartmentController;
import com.a6raywa1cher.db_rgr.terminal.controller.MainMenuController;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class TerminalClient implements AutoCloseable {
	private final Map<Class<? extends Controller>, Controller> controllerMap = new HashMap<>();

	private final ClientEnvironment clientEnvironment;

	private final EntityManager entityManager;

	private final Scanner scanner;

	private final PrintWriter writer;

	public TerminalClient(EntityManager entityManager, InputStream inputStream, OutputStream outputStream) {
		this.entityManager = entityManager;
		this.scanner = new Scanner(inputStream);
		this.writer = new PrintWriter(outputStream, true);
		this.clientEnvironment = new ClientEnvironment(scanner, writer, entityManager, new HashSet<>());
		addDefaultControllers();
	}

	private void addDefaultControllers() {
		controllerMap.put(MainMenuController.class, new MainMenuController(clientEnvironment));
		controllerMap.put(DepartmentController.class, new DepartmentController(clientEnvironment));
	}

	public void start() {
		Result result = new Result(MainMenuController.class, Controller.MAIN_METHOD);
		while (result.nextController() != null) {
			Controller controller = controllerMap.get(result.nextController());
			result = controller.process(result.nextMethod());
			clientEnvironment.setBag(result.newBag());
		}
	}

	@Override
	public void close() throws Exception {
		scanner.close();
		writer.close();
	}
}
