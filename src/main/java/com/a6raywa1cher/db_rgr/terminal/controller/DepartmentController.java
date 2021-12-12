package com.a6raywa1cher.db_rgr.terminal.controller;

import com.a6raywa1cher.db_rgr.model.Department;
import com.a6raywa1cher.db_rgr.model.repository.DepartmentRepository;
import com.a6raywa1cher.db_rgr.terminal.AbstractCrudMenuController;
import com.a6raywa1cher.db_rgr.terminal.ClientEnvironment;

public class DepartmentController extends AbstractCrudMenuController<Department> {
	public DepartmentController(ClientEnvironment clientEnvironment) {
		super(
			clientEnvironment,
			Department.class,
			MainMenuController.class,
			new DepartmentRepository(clientEnvironment.getEntityManager())
		);
	}

	@Override
	protected Department example() {
		return new Department("Central department of Tver", "+7 123 432-23-55", "Tver");
	}
}
