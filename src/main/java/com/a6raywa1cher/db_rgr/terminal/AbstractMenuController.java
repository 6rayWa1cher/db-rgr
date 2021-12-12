package com.a6raywa1cher.db_rgr.terminal;

import lombok.Data;

import java.io.PrintWriter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class AbstractMenuController implements Controller {
	protected final Class<? extends Controller> exitController;
	protected final ClientEnvironment clientEnvironment;
	protected final Scanner scanner;
	protected final PrintWriter writer;
	private final Map<String, InnerMethod> methods = new HashMap<>();

	public AbstractMenuController(Class<? extends Controller> exitController, ClientEnvironment clientEnvironment) {
		this.exitController = exitController;
		this.clientEnvironment = clientEnvironment;
		this.scanner = clientEnvironment.getScanner();
		this.writer = clientEnvironment.getPrintWriter();
		registerDefault();
	}

	protected abstract String getName();

	protected void registerDefault() {
		registerMethod(Controller.MAIN_METHOD, null, null, this::menu);
	}

	protected void registerMethod(String methodName, String methodKey, String methodVisibleName, Supplier<Result> method) {
		Objects.requireNonNull(methodName);
		Objects.requireNonNull(method);
		methods.put(methodName, new InnerMethod(methodName, methodKey, methodVisibleName, method));
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
		return buildPrompt().prefix(prefix).prompt();
	}

	public Result menu() {
		writer.println(getName());
		writer.println("Choose next:");
		Map<String, InnerMethod> filteredMap = methods.values()
			.stream()
			.filter(innerMethod -> innerMethod.methodVisibleName() != null)
			.collect(Collectors.toMap(InnerMethod::menuKey, m -> m));

		filteredMap.values()
			.stream()
			.sorted(Comparator.comparing(InnerMethod::methodName))
			.forEach(m -> writer.println("[" + m.menuKey() + "]\t" + m.methodVisibleName()));
		writer.println("[e]\tExit");

//		String input = promptUntil("", s -> s.equals("e") || filteredMap.containsKey(s));
		String input = buildPrompt()
			.validator(s -> s.equals("e") || filteredMap.containsKey(s))
			.prompt();
		if (input.equals("e")) {
			if (exitController != null) {
				return new Result(exitController, Controller.MAIN_METHOD, Collections.emptySet());
			} else {
				return new Result(null, null);
			}
		}
		InnerMethod innerMethod = filteredMap.get(input);
		writer.println();
		return innerMethod.method().get();
	}

	protected boolean confirm() {
		writer.println("Confirm:");
		writer.println("[y] Yes");
		writer.println("[n] No");
		String b = buildPrompt(String.class)
			.preMapper(String::toLowerCase)
			.validator(s -> s.equals("y") || s.equals("n"))
			.prompt();
		return b.equals("y");
	}

	protected Prompt<String> buildPrompt() {
		return buildPrompt(String.class);
	}

	protected <J> Prompt<J> buildPrompt(Class<J> clazz) {
		return new Prompt<>(clazz);
	}

	private record InnerMethod(
		String methodName,
		String menuKey,
		String methodVisibleName,
		Supplier<Result> method
	) {

	}

	@Data
	protected class Prompt<T> {
		private final Class<T> targetClass;
		private String prefix = "";
		private Predicate<T> validator = t -> true;
		private BiConsumer<T, Exception> errorFunc = (t, e) -> {
			if (e != null) {
				writer.println("Invalid input: " + e.getMessage());
			} else {
				writer.println("Invalid input");
			}
		};
		private Function<String, T> preMapper;

		private Prompt(Class<T> targetClass) {
			this.targetClass = targetClass;
			this.preMapper = (s) -> clientEnvironment.getStringParser().parse(s, targetClass);
		}

		public Prompt<T> prefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

		public Prompt<T> preMapper(Function<String, T> preMapper) {
			this.preMapper = preMapper;
			return this;
		}

		public Prompt<T> validator(Predicate<T> validator) {
			this.validator = validator;
			return this;
		}

		public Prompt<T> errorFunc(BiConsumer<T, Exception> errorFunc) {
			this.errorFunc = errorFunc;
			return this;
		}

		public Prompt<T> errorMessage(String errorMessage) {
			this.errorFunc = (t, e) -> writer.println(errorMessage);
			return this;
		}

		public T prompt() {
			while (true) {
				writer.print(prefix);
				writer.print("> ");
				writer.flush();
				String s = scanner.nextLine().strip();
				try {
					T o = preMapper.apply(s);
					boolean valid = validator.test(o);
					if (valid) {
						return o;
					} else {
						errorFunc.accept(o, null);
					}
				} catch (Exception e) {
					errorFunc.accept(null, e);
				}
			}
		}
	}
}
