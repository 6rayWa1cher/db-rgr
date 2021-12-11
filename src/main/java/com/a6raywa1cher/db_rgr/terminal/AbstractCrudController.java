package com.a6raywa1cher.db_rgr.terminal;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.dblib.analyzer.ClassData;
import com.a6raywa1cher.db_rgr.dblib.entity.Entity;

import java.util.List;

public abstract class AbstractCrudController<T extends Entity> extends AbstractController {
	protected final ClassData classData;

	protected final Class<T> entityClass;

	protected final CrudRepository<T> crudRepository;

	protected final EntityManager entityManager;

	public AbstractCrudController(ClientEnvironment clientEnvironment,
								  Class<T> entityClass,
								  Class<? extends Controller> exitController,
								  CrudRepository<T> crudRepository) {
		super(exitController, clientEnvironment);
		this.entityManager = clientEnvironment.getEntityManager();
		this.entityClass = entityClass;
		this.classData = entityManager.getClassAnalyzer().getClassData(entityClass);
		this.crudRepository = crudRepository;
	}

	@Override
	protected void registerDefault() {
		super.registerDefault();
		registerMethod("getAll", "Get all", this::getAll);
	}

	@Override
	protected String getName() {
		return entityClass.getSimpleName();
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
}
