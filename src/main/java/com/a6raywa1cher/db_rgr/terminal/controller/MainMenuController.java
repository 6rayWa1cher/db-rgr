package com.a6raywa1cher.db_rgr.terminal.controller;

import com.a6raywa1cher.db_rgr.terminal.AbstractMenuController;
import com.a6raywa1cher.db_rgr.terminal.ClientEnvironment;
import com.a6raywa1cher.db_rgr.terminal.Controller;
import com.a6raywa1cher.db_rgr.terminal.Result;

public class MainMenuController extends AbstractMenuController {
	public MainMenuController(ClientEnvironment clientEnvironment) {
		super(null, clientEnvironment);
	}

	@Override
	protected void registerDefault() {
		super.registerDefault();
		registerMethod("department", "d", "Department", this::department);
	}

	@Override
	protected String getName() {
		return "Main menu";
	}

	public Result department() {
		return new Result(DepartmentController.class, Controller.MAIN_METHOD);
	}
}
