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
		registerMethod("employee", "e", "Employee", this::employee);
		registerMethod("employeeRequirement", "er", "Employee Requirement", this::employeeRequirement);
		registerMethod("employeeType", "et", "Employee Type", this::employeeType);
		registerMethod("machinery", "m", "Machinery", this::machinery);
		registerMethod("machineryRequirement", "mr", "Machinery Requirement", this::machineryRequirement);
		registerMethod("machineryType", "mt", "Machinery Type", this::machineryType);
	}

	@Override
	protected String getName() {
		return "Main menu";
	}

	public Result department() {
		return new Result(DepartmentController.class, Controller.MAIN_METHOD);
	}

	public Result employee() {
		return new Result(EmployeeController.class, Controller.MAIN_METHOD);
	}

	public Result employeeRequirement() {
		return new Result(EmployeeRequirementController.class, Controller.MAIN_METHOD);
	}

	public Result employeeType() {
		return new Result(EmployeeTypeController.class, Controller.MAIN_METHOD);
	}

	public Result machinery() {
		return new Result(MachineryController.class, Controller.MAIN_METHOD);
	}

	public Result machineryRequirement() {
		return new Result(MachineryRequirementController.class, Controller.MAIN_METHOD);
	}

	public Result machineryType() {
		return new Result(MachineryTypeController.class, Controller.MAIN_METHOD);
	}
}
