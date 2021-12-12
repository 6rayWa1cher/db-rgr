package com.a6raywa1cher.db_rgr.terminal.controller;

import com.a6raywa1cher.db_rgr.model.EmployeeType;
import com.a6raywa1cher.db_rgr.model.repository.EmployeeTypeRepository;
import com.a6raywa1cher.db_rgr.terminal.AbstractCrudMenuController;
import com.a6raywa1cher.db_rgr.terminal.ClientEnvironment;

public class EmployeeTypeController extends AbstractCrudMenuController<EmployeeType> {
	public EmployeeTypeController(ClientEnvironment clientEnvironment) {
		super(
			clientEnvironment,
			EmployeeType.class,
			MainMenuController.class,
			new EmployeeTypeRepository(clientEnvironment.getEntityManager())
		);
	}

	@Override
	protected EmployeeType example() {
		return new EmployeeType("Major", "Policeman", 5);
	}
}
