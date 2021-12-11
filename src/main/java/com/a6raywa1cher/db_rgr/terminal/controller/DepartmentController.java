package com.a6raywa1cher.db_rgr.terminal.controller;

import com.a6raywa1cher.db_rgr.model.Department;
import com.a6raywa1cher.db_rgr.model.repository.DepartmentRepository;
import com.a6raywa1cher.db_rgr.terminal.AbstractCrudController;
import com.a6raywa1cher.db_rgr.terminal.ClientEnvironment;

public class DepartmentController extends AbstractCrudController<Department> {
	public DepartmentController(ClientEnvironment clientEnvironment) {
		super(
			clientEnvironment,
			Department.class,
			MainMenuController.class,
			new DepartmentRepository(clientEnvironment.getEntityManager())
		);
	}
}
