package com.a6raywa1cher.db_rgr.terminal;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.dblib.analyzer.ClassData;
import com.a6raywa1cher.db_rgr.dblib.analyzer.FieldData;
import com.a6raywa1cher.db_rgr.dblib.entity.Entity;
import com.a6raywa1cher.db_rgr.lib.Pair;
import com.a6raywa1cher.db_rgr.lib.ReflectionUtils;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;

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
		registerMethod("getById", "g", "Get by id", this::getById);
		registerMethod("getAll", "G", "Get all", this::getAll);
		registerMethod("add", "a", "Add", this::add);
		registerMethod("update", "u", "Update", this::update);
		registerMethod("refresh", "r", "Refresh", this::refresh);
		registerMethod("delete", "d", "Delete", this::delete);
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
			Object data = buildPrompt(fieldData.type()).prefix(fieldData.fieldName()).bypassNulls().prompt();
			fieldData.setter().invoke(t, data);
		}
		return t;
	}

	protected Object[] readPrimaryKeyFromConsole() {
		List<FieldData> primaryKey = classData.getPrimaryKey();
		Object[] params = new Object[primaryKey.size()];
		for (int i = 0; i < primaryKey.size(); i++) {
			FieldData fieldData = primaryKey.get(i);
			Object data = buildPrompt(fieldData.type()).prefix(fieldData.fieldName()).prompt();
			params[i] = data;
		}
		return params;
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

	protected Result withTransaction(Supplier<Result> supplier) {
		entityManager.openTransaction();
		try {
			Result out = supplier.get();
			entityManager.commit();
			return out;
		} catch (Exception e) {
			entityManager.rollback();
			//noinspection ConstantConditions
			if (SQLException.class.isAssignableFrom(e.getClass())) {
				writer.println("SQLException occur: " + e.getMessage());
				return new Result(getClass(), Controller.MAIN_METHOD);
			} else {
				throw e;
			}
		}
	}

	public Result add() {
		writer.println("Type fields");
		writer.println("Example: ");
		writer.println(example().toString());
		T t = readEntityFromConsole();
		writer.println("You typed:");
		writer.println(t);
		boolean c = confirm();
		if (!c) {
			writer.println("Aborted");
			return new Result(getClass(), Controller.MAIN_METHOD);
		}
		return withTransaction(() -> {
			crudRepository.insert(t);
			writer.println("Appended");
			return new Result(getClass(), Controller.MAIN_METHOD);
		});
	}

	@SneakyThrows({
		IllegalAccessException.class,
		IllegalArgumentException.class,
		InvocationTargetException.class
	})
	protected void updateObject(T t) {
		for (FieldData fieldData : classData.getFieldDataList()) {
			Object prevValue = fieldData.getter().invoke(t);
			Pair<?, Boolean> pair = buildPrompt(fieldData.type())
				.prefix(fieldData.fieldName() + "[" + prevValue.toString() + "]")
				.bypassNulls()
				.promptExtended();
			Object value = pair.left();
			boolean isValuePresent = pair.right();
			if (isValuePresent) {
				fieldData.setter().invoke(t, value);
			}
		}
	}

	public Result update() {
		writer.println("Type primary key");
		Object[] pk = readPrimaryKeyFromConsole();
		T t = crudRepository.getById(pk);
		if (t == null) {
			writer.println(entityClass.getSimpleName() + " isn't found");
			return new Result(getClass(), Controller.MAIN_METHOD);
		}
		writer.println("Type new values (type null for erase)");
		updateObject(t);
		writer.println("Final object:");
		writer.println(t);
		boolean c = confirm();
		if (!c) {
			writer.println("Aborted");
			return new Result(getClass(), Controller.MAIN_METHOD);
		}
		return withTransaction(() -> {
			crudRepository.update(t);
			writer.println("Appended");
			return new Result(getClass(), Controller.MAIN_METHOD);
		});
	}

	public Result delete() {
		writer.println("Type primary key");
		Object[] pk = readPrimaryKeyFromConsole();
		T t = crudRepository.getById(pk);
		if (t == null) {
			writer.println(entityClass.getSimpleName() + " isn't found");
			return new Result(getClass(), Controller.MAIN_METHOD);
		}
		writer.println(t);
		boolean c = confirm();
		if (!c) {
			writer.println("Aborted");
			return new Result(getClass(), Controller.MAIN_METHOD);
		}
		return withTransaction(() -> {
			crudRepository.delete(t);
			writer.println("Deleted");
			return new Result(getClass(), Controller.MAIN_METHOD);
		});
	}

	public Result getById() {
		writer.println("Type primary key");
		Object[] pk = readPrimaryKeyFromConsole();
		T t = crudRepository.getById(pk);
		if (t == null) {
			writer.println(entityClass.getSimpleName() + " isn't found");
			return new Result(getClass(), Controller.MAIN_METHOD);
		}
		writer.println(t);
		return new Result(getClass(), Controller.MAIN_METHOD);
	}
}
