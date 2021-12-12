package com.a6raywa1cher.db_rgr.terminal.controller;

import com.a6raywa1cher.db_rgr.model.Department;
import com.a6raywa1cher.db_rgr.model.repository.DepartmentRepository;
import com.a6raywa1cher.db_rgr.terminal.AbstractCrudMenuController;
import com.a6raywa1cher.db_rgr.terminal.ClientEnvironment;
import com.a6raywa1cher.db_rgr.terminal.Controller;
import com.a6raywa1cher.db_rgr.terminal.Result;

import java.util.List;
import java.util.Objects;

public class DepartmentController extends AbstractCrudMenuController<Department> {
	private final DepartmentRepository repository;
	public DepartmentController(ClientEnvironment clientEnvironment) {
		super(
			clientEnvironment,
			Department.class,
			MainMenuController.class,
			new DepartmentRepository(clientEnvironment.getEntityManager())
		);

		this.repository = (DepartmentRepository) crudRepository;
	}

	@Override
	protected void registerDefault() {
		super.registerDefault();
		registerMethod("getEmployeeStats", "es", "Employee Stats", this::getEmployeeStats);
		registerMethod("getMachineryStats", "ms", "Machinery Stats", this::getMachineryStats);
	}

	@Override
	protected Department example() {
		return new Department("Central department of Tver city", "+7 123 432-23-55", "Tver");
	}

	public Result getEmployeeStats() {
		writer.println("Type primary key");
		Object[] pk = readPrimaryKeyFromConsole();
		Department t = crudRepository.getById(pk);
		if (t == null) {
			writer.println(entityClass.getSimpleName() + " isn't found");
			return new Result(getClass(), Controller.MAIN_METHOD);
		}
		List<DepartmentRepository.EmployeeTypeStats> stats = repository.getEmployeeTypeStats(t);
		List<DepartmentRepository.EmployeeTypeStats> satisfied = stats.stream()
			.filter(s -> Objects.equals(s.getExpectedCount(), s.getRealCount()))
			.toList();
		writer.println("Satisfied");
		for (var stat : satisfied) {
			writer.println(stat);
		}
		writer.println();
		List<DepartmentRepository.EmployeeTypeStats> overSatisfied = stats.stream()
			.filter(s -> s.getExpectedCount() < s.getRealCount())
			.toList();
		writer.println("Over satisfied");
		for (var stat : overSatisfied) {
			writer.println(stat);
		}
		writer.println();
		List<DepartmentRepository.EmployeeTypeStats> notSatisfied = stats.stream()
			.filter(s -> s.getExpectedCount() > s.getRealCount())
			.toList();
		writer.println("Not satisfied");
		for (var stat : notSatisfied) {
			writer.println(stat);
		}
		writer.println();
		return new Result(getClass(), Controller.MAIN_METHOD);
	}

	public Result getMachineryStats() {
		writer.println("Type primary key");
		Object[] pk = readPrimaryKeyFromConsole();
		Department t = crudRepository.getById(pk);
		if (t == null) {
			writer.println(entityClass.getSimpleName() + " isn't found");
			return new Result(getClass(), Controller.MAIN_METHOD);
		}
		List<DepartmentRepository.MachineryTypeStats> stats = repository.getMachineryTypeStats(t);
		List<DepartmentRepository.MachineryTypeStats> satisfied = stats.stream()
			.filter(s -> Objects.equals(s.getExpectedCount(), s.getRealCount()))
			.toList();
		writer.println("Satisfied");
		for (var stat : satisfied) {
			writer.println(stat);
		}
		writer.println();
		List<DepartmentRepository.MachineryTypeStats> overSatisfied = stats.stream()
			.filter(s -> s.getExpectedCount() < s.getRealCount())
			.toList();
		writer.println("Over satisfied");
		for (var stat : overSatisfied) {
			writer.println(stat);
		}
		writer.println();
		List<DepartmentRepository.MachineryTypeStats> notSatisfied = stats.stream()
			.filter(s -> s.getExpectedCount() > s.getRealCount())
			.toList();
		writer.println("Not satisfied");
		for (var stat : notSatisfied) {
			writer.println(stat);
		}
		writer.println();
		return new Result(getClass(), Controller.MAIN_METHOD);
	}
}
