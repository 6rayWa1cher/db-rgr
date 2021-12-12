package com.a6raywa1cher.db_rgr.terminal.controller;

import com.a6raywa1cher.db_rgr.model.Machinery;
import com.a6raywa1cher.db_rgr.model.repository.MachineryRepository;
import com.a6raywa1cher.db_rgr.terminal.AbstractCrudMenuController;
import com.a6raywa1cher.db_rgr.terminal.ClientEnvironment;

import java.time.LocalDate;

public class MachineryController extends AbstractCrudMenuController<Machinery> {
	public MachineryController(ClientEnvironment clientEnvironment) {
		super(
			clientEnvironment,
			Machinery.class,
			MainMenuController.class,
			new MachineryRepository(clientEnvironment.getEntityManager())
		);
	}

	@Override
	protected Machinery example() {
		return new Machinery("Central department of Tver city", 1, "Lada Vesta", "Ivanov Ivan Ivanovich", LocalDate.now().minusYears(1));
	}
}
