package com.a6raywa1cher.db_rgr.terminal;

import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.terminal.controller.*;

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

	private final StringParser stringParser;

	private final EntityManager entityManager;

	private final Scanner scanner;

	private final PrintWriter writer;

	public TerminalClient(EntityManager entityManager, InputStream inputStream, OutputStream outputStream) {
		this.entityManager = entityManager;
		this.scanner = new Scanner(inputStream);
		this.writer = new PrintWriter(outputStream, true);
		this.stringParser = new StringParser();
		this.clientEnvironment = new ClientEnvironment(scanner, writer, entityManager, stringParser, new HashSet<>());
		addDefaultControllers();
	}

	private void addDefaultControllers() {
		controllerMap.put(MainMenuController.class, new MainMenuController(clientEnvironment));
		controllerMap.put(DepartmentController.class, new DepartmentController(clientEnvironment));
		controllerMap.put(EmployeeController.class, new EmployeeController(clientEnvironment));
		controllerMap.put(EmployeeTypeController.class, new EmployeeTypeController(clientEnvironment));
		controllerMap.put(EmployeeRequirementController.class, new EmployeeRequirementController(clientEnvironment));
		controllerMap.put(MachineryController.class, new MachineryController(clientEnvironment));
		controllerMap.put(MachineryTypeController.class, new MachineryTypeController(clientEnvironment));
		controllerMap.put(MachineryRequirementController.class, new MachineryRequirementController(clientEnvironment));
	}

	public void start() {
		Result result = new Result(MainMenuController.class, Controller.MAIN_METHOD);
		while (result.nextController() != null) {
			try {
				Controller controller = controllerMap.get(result.nextController());
				result = controller.process(result.nextMethod());
				clientEnvironment.setBag(result.newBag());
			} catch (Exception e) {
				writer.println("Exception has occured");
				e.printStackTrace(writer);
				result = new Result(MainMenuController.class, Controller.MAIN_METHOD);
			}
		}
	}

	@Override
	public void close() throws Exception {
		scanner.close();
		writer.close();
	}
}
