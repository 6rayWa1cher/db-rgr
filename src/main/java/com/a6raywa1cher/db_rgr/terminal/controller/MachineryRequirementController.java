package com.a6raywa1cher.db_rgr.terminal.controller;

import com.a6raywa1cher.db_rgr.model.MachineryRequirement;
import com.a6raywa1cher.db_rgr.model.repository.MachineryRequirementRepository;
import com.a6raywa1cher.db_rgr.terminal.AbstractCrudMenuController;
import com.a6raywa1cher.db_rgr.terminal.ClientEnvironment;

public class MachineryRequirementController extends AbstractCrudMenuController<MachineryRequirement> {
	public MachineryRequirementController(ClientEnvironment clientEnvironment) {
		super(
			clientEnvironment,
			MachineryRequirement.class,
			MainMenuController.class,
			new MachineryRequirementRepository(clientEnvironment.getEntityManager())
		);
	}

	@Override
	protected MachineryRequirement example() {
		return new MachineryRequirement("Central department of Tver city", "Lada Vesta", 2);
	}
}
