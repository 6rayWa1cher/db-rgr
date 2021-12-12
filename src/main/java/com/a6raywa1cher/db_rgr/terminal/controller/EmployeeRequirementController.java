package com.a6raywa1cher.db_rgr.terminal.controller;

import com.a6raywa1cher.db_rgr.model.EmployeeRequirement;
import com.a6raywa1cher.db_rgr.model.repository.EmployeeRequirementRepository;
import com.a6raywa1cher.db_rgr.terminal.AbstractCrudMenuController;
import com.a6raywa1cher.db_rgr.terminal.ClientEnvironment;

public class EmployeeRequirementController extends AbstractCrudMenuController<EmployeeRequirement> {
	public EmployeeRequirementController(ClientEnvironment clientEnvironment) {
		super(
			clientEnvironment,
			EmployeeRequirement.class,
			MainMenuController.class,
			new EmployeeRequirementRepository(clientEnvironment.getEntityManager())
		);
	}

	@Override
	protected EmployeeRequirement example() {
		return new EmployeeRequirement("Central department of Tver city", "Major", "Policeman", 5);
	}
}
