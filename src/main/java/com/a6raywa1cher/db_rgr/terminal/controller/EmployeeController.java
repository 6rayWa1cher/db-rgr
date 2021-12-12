package com.a6raywa1cher.db_rgr.terminal.controller;

import com.a6raywa1cher.db_rgr.model.Employee;
import com.a6raywa1cher.db_rgr.model.repository.EmployeeRepository;
import com.a6raywa1cher.db_rgr.terminal.AbstractCrudMenuController;
import com.a6raywa1cher.db_rgr.terminal.ClientEnvironment;

import java.time.LocalDate;

public class EmployeeController extends AbstractCrudMenuController<Employee> {
	public EmployeeController(ClientEnvironment clientEnvironment) {
		super(
			clientEnvironment,
			Employee.class,
			MainMenuController.class,
			new EmployeeRepository(clientEnvironment.getEntityManager())
		);
	}

	@Override
	protected Employee example() {
		return new Employee("Ivanov Ivan Ivanovich", "Central department of Tver city",
			"Policeman", "Major", LocalDate.now().minusYears(35),
			LocalDate.now().minusYears(10), LocalDate.now().minusYears(2));
	}
}
