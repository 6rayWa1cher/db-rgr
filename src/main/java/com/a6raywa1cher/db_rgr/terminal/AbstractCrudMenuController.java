package com.a6raywa1cher.db_rgr.terminal;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.dblib.analyzer.ClassData;
import com.a6raywa1cher.db_rgr.dblib.analyzer.FieldData;
import com.a6raywa1cher.db_rgr.dblib.entity.Entity;
import com.a6raywa1cher.db_rgr.lib.ReflectionUtils;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public abstract class AbstractCrudMenuController<T extends Entity> extends AbstractMenuController {
	protected final ClassData classData;

	protected final Class<T> entityClass;

	protected final CrudRepository<T> crudRepository;

	protected final EntityManager entityManager;

	protected final StringParser stringParser;

	public AbstractCrudMenuController(ClientEnvironment clientEnvironment,
									  Class<T> entityClass,
									  Class<? extends Controller> exitController,
									  CrudRepository<T> crudRepository) {
		super(exitController, clientEnvironment);
		this.entityManager = clientEnvironment.getEntityManager();
		this.entityClass = entityClass;
		this.classData = entityManager.getClassAnalyzer().getClassData(entityClass);
		this.stringParser = clientEnvironment.getStringParser();
		this.crudRepository = crudRepository;
	}

	@Override
	protected void registerDefault() {
		super.registerDefault();
		registerMethod("getAll", "G", "Get all", this::getAll);
		registerMethod("add", "a", "Add", this::add);
		registerMethod("refresh", "r", "Refresh", this::refresh);
	}

	@Override
	protected String getName() {
		return entityClass.getSimpleName() + "(" + crudRepository.count() + ")";
	}

	public Result refresh() {
		return new Result(getClass(), Controller.MAIN_METHOD);
	}

	@SneakyThrows({
		IllegalAccessException.class,
		IllegalArgumentException.class,
		InvocationTargetException.class
	})
	protected T readEntityFromConsole() {
		T t = ReflectionUtils.instantiate(entityClass);
		for (FieldData fieldData : classData.getFieldDataList()) {
			Object data = buildPrompt(fieldData.type()).prefix(fieldData.fieldName()).prompt();
			fieldData.setter().invoke(t, data);
		}
		return t;
	}

	public Result getAll() {
		List<T> entities = crudRepository.getAll();
		if (entities.size() > 0) {
			entities.forEach(System.out::println);
		} else {
			writer.println("No results");
		}
		writer.println();
		return new Result(this.getClass(), Controller.MAIN_METHOD);
	}

	protected abstract T example();

	public Result add() {
		writer.println("Type fields");
		writer.println("Example: ");
		writer.println(example().toString());
		T t = readEntityFromConsole();
		writer.println("You typed:");
		writer.println(t.toString());
		boolean c = confirm();
		if (!c) {
			writer.println("Aborted");
			return new Result(getClass(), Controller.MAIN_METHOD);
		}
		crudRepository.insert(t);
		writer.println("Appended");
		return new Result(getClass(), Controller.MAIN_METHOD);
	}
}
