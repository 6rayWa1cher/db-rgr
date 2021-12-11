package com.a6raywa1cher.db_rgr.terminal;

import java.io.PrintWriter;
import java.util.*;
import java.util.function.Supplier;

public abstract class AbstractController implements Controller {
	protected final Class<? extends Controller> exitController;
	protected final ClientEnvironment clientEnvironment;
	protected final Scanner scanner;
	protected final PrintWriter writer;
	private final Map<String, InnerMethod> methods = new HashMap<>();

	public AbstractController(Class<? extends Controller> exitController, ClientEnvironment clientEnvironment) {
		this.exitController = exitController;
		this.clientEnvironment = clientEnvironment;
		this.scanner = clientEnvironment.getScanner();
		this.writer = clientEnvironment.getPrintWriter();
		registerDefault();
	}

	protected abstract String getName();

	protected void registerDefault() {
		registerMethod(Controller.MAIN_METHOD, null, this::menu);
	}

	protected void registerMethod(String methodName, String methodVisibleName, Supplier<Result> method) {
		Objects.requireNonNull(methodName);
		Objects.requireNonNull(method);
		methods.put(methodName, new InnerMethod(methodName, methodVisibleName, method));
	}

	@Override
	public Result process(String method) {
		Supplier<Result> function = Optional.of(methods)
			.map(m -> m.get(method))
			.map(InnerMethod::method)
			.orElse(this::menu);
		return function.get();
	}

	protected String prompt() {
		return prompt("");
	}

	protected String prompt(String prefix) {
		writer.print(prefix);
		writer.print("> ");
		writer.flush();
		return scanner.next().toLowerCase(Locale.ROOT).strip();
	}

	public Result menu() {
		writer.println(getName());
		writer.println("Choose next:");
		Map<String, InnerMethod> map = new HashMap<>();
		List<InnerMethod> list = methods.entrySet()
			.stream()
			.sorted(Map.Entry.comparingByKey())
			.map(Map.Entry::getValue)
			.toList();
		for (int i = 0; i < list.size(); i++) {
			map.put("" + i, list.get(i));
		}
		for (int i = 0; i < list.size(); i++) {
			InnerMethod innerMethod = list.get(i);
			writer.println("[" + i + "]\t" + innerMethod.methodVisibleName());
		}
		writer.println("[e]\tExit");

		while (true) {
			String input = prompt();
			if (input.equals("e")) {
				if (exitController != null) {
					return new Result(exitController, Controller.MAIN_METHOD, Collections.emptySet());
				} else {
					return new Result(null, null);
				}
			}
			InnerMethod innerMethod = map.get(input);
			if (innerMethod == null) {
				writer.println("Incorrect input");
				continue;
			}
			return innerMethod.method().get();
		}
	}

	private record InnerMethod(
		String methodName,
		String methodVisibleName,
		Supplier<Result> method
	) {

	}
}
