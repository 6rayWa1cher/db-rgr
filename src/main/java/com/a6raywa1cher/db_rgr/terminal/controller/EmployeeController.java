package com.a6raywa1cher.db_rgr.terminal.controller;

import com.a6raywa1cher.db_rgr.model.Employee;
import com.a6raywa1cher.db_rgr.model.Machinery;
import com.a6raywa1cher.db_rgr.model.repository.EmployeeRepository;
import com.a6raywa1cher.db_rgr.model.repository.MachineryRepository;
import com.a6raywa1cher.db_rgr.terminal.AbstractCrudMenuController;
import com.a6raywa1cher.db_rgr.terminal.ClientEnvironment;
import com.a6raywa1cher.db_rgr.terminal.Controller;
import com.a6raywa1cher.db_rgr.terminal.Result;

import java.time.LocalDate;
import java.util.List;

public class EmployeeController extends AbstractCrudMenuController<Employee> {
	private final MachineryRepository machineryRepository;

	public EmployeeController(ClientEnvironment clientEnvironment) {
		super(
			clientEnvironment,
			Employee.class,
			MainMenuController.class,
			new EmployeeRepository(clientEnvironment.getEntityManager())
		);
		this.machineryRepository = new MachineryRepository(clientEnvironment.getEntityManager());
	}

	@Override
	protected void registerDefault() {
		super.registerDefault();
		registerMethod("getAllOwnedMachinery", "m", "Get all owned machinery", this::getAllOwnedMachinery);
	}

	@Override
	protected Employee example() {
		return new Employee("Ivanov Ivan Ivanovich", "Central department of Tver city",
			"Policeman", "Major", LocalDate.now().minusYears(35),
			LocalDate.now().minusYears(10), LocalDate.now().minusYears(2));
	}

	public Result getAllOwnedMachinery() {
		return getEntityOrMain((e) -> {
			List<Machinery> list = machineryRepository.getAllByHolder(e);
			if (list.size() > 0) {
				list.forEach(writer::println);
			} else {
				writer.println("No results");
			}
			return new Result(getClass(), Controller.MAIN_METHOD);
		});
	}
}
