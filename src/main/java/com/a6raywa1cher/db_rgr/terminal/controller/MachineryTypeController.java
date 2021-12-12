package com.a6raywa1cher.db_rgr.terminal.controller;

import com.a6raywa1cher.db_rgr.model.MachineryType;
import com.a6raywa1cher.db_rgr.model.repository.MachineryTypeRepository;
import com.a6raywa1cher.db_rgr.terminal.AbstractCrudMenuController;
import com.a6raywa1cher.db_rgr.terminal.ClientEnvironment;

public class MachineryTypeController extends AbstractCrudMenuController<MachineryType> {
	public MachineryTypeController(ClientEnvironment clientEnvironment) {
		super(
			clientEnvironment,
			MachineryType.class,
			MainMenuController.class,
			new MachineryTypeRepository(clientEnvironment.getEntityManager())
		);
	}

	@Override
	protected MachineryType example() {
		return new MachineryType("Lada Vesta", "car", 10);
	}
}
