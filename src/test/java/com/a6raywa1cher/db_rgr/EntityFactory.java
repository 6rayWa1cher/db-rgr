package com.a6raywa1cher.db_rgr;

import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.model.*;
import com.a6raywa1cher.db_rgr.model.repository.*;

import java.time.LocalDate;
import java.util.function.Supplier;

public class EntityFactory {
	private final DepartmentRepository departmentRepository;
	private final EmployeeRepository employeeRepository;
	private final EmployeeRequirementRepository employeeRequirementRepository;
	private final EmployeeTypeRepository employeeTypeRepository;
	private final MachineryRepository machineryRepository;
	private final MachineryRequirementRepository machineryRequirementRepository;
	private final MachineryTypeRepository machineryTypeRepository;
	private int i = 0;

	public EntityFactory(EntityManager em) {
		this.departmentRepository = new DepartmentRepository(em);
		this.employeeRepository = new EmployeeRepository(em);
		this.employeeRequirementRepository = new EmployeeRequirementRepository(em);
		this.employeeTypeRepository = new EmployeeTypeRepository(em);
		this.machineryRepository = new MachineryRepository(em);
		this.machineryRequirementRepository = new MachineryRequirementRepository(em);
		this.machineryTypeRepository = new MachineryTypeRepository(em);
	}

	private <T> T first(T left, T right) {
		return left != null ? left : right;
	}

	private <T> T first(T left, Supplier<T> right) {
		return left != null ? left : right.get();
	}

	public Department buildDepartment(Department facade) {
		Department department = new Department();
		department.setTitle(first(facade.getTitle(), "t#" + ++i));
		department.setAddress(first(facade.getAddress(), "a#" + ++i));
		department.setTelephoneNumber(first(facade.getTelephoneNumber(), "tn#" + ++i));
		return department;
	}

	public EmployeeType buildEmployeeType(EmployeeType facade) {
		EmployeeType employeeType = new EmployeeType();
		employeeType.setEmployeeRank(first(facade.getEmployeeRank(), "er#" + ++i));
		employeeType.setPosition(first(facade.getPosition(), "p#" + ++i));
		employeeType.setRetirementAfterYears(first(facade.getRetirementAfterYears(), ++i % 10 + 3));
		return employeeType;
	}

	public EmployeeRequirement buildEmployeeRequirement(EmployeeRequirement facade) {
		EmployeeRequirement requirement = new EmployeeRequirement();
		requirement.setDepartmentTitle(first(facade.getDepartmentTitle(), () -> createDepartment().getTitle()));
		if (facade.getPosition() != null) {
			requirement.setPosition(facade.getPosition());
			requirement.setEmployeeRank(facade.getEmployeeRank());
		} else {
			EmployeeType employeeType = createEmployeeType();
			requirement.setPosition(employeeType.getPosition());
			requirement.setEmployeeRank(employeeType.getEmployeeRank());
		}
		requirement.setCount(first(facade.getCount(), ++i % 10 + 2));
		return requirement;
	}

	public Employee buildEmployee(Employee facade) {
		Employee employee = new Employee();
		employee.setDepartmentTitle(first(facade.getDepartmentTitle(), () -> createDepartment().getTitle()));
		employee.setFullName(first(facade.getFullName(), "fn#" + ++i));
		employee.setBirthDate(first(facade.getBirthDate(), LocalDate.now().minusYears((++i % 30) + 20)));
		if (facade.getPosition() != null) {
			employee.setPosition(facade.getPosition());
			employee.setEmployeeRank(facade.getEmployeeRank());
		} else {
			EmployeeType employeeType = createEmployeeType();
			employee.setPosition(employeeType.getPosition());
			employee.setEmployeeRank(employeeType.getEmployeeRank());
		}
		employee.setLastPromotionDate(first(facade.getLastPromotionDate(), LocalDate.now().minusMonths((++i % 12) + 1)));
		employee.setEmploymentDate(first(facade.getEmploymentDate(), LocalDate.now().minusYears((++i % 12))));
		return employee;
	}

	public MachineryType buildMachineryType(MachineryType facade) {
		MachineryType machineryType = new MachineryType();
		machineryType.setMachineryTitle(first(facade.getMachineryTitle(), "mt#" + ++i));
		machineryType.setType(first(facade.getType(), "t#" + ++i));
		machineryType.setLifeTimeYears(first(facade.getLifeTimeYears(), ++i % 10 + 3));
		return machineryType;
	}

	public MachineryRequirement buildMachineryRequirement(MachineryRequirement facade) {
		MachineryRequirement requirement = new MachineryRequirement();
		requirement.setMachineryTitle(first(facade.getMachineryTitle(), () -> createMachineryType().getMachineryTitle()));
		requirement.setDepartmentTitle(first(facade.getDepartmentTitle(), () -> createDepartment().getTitle()));
		requirement.setCount(first(facade.getCount(), ++i % 10 + 2));
		return requirement;
	}

	public Machinery buildMachinery(Machinery facade) {
		Machinery machinery = new Machinery();
		machinery.setId(first(facade.getId(), ++i));
		machinery.setMachineryTitle(first(facade.getMachineryTitle(), () -> createMachineryType().getMachineryTitle()));
		machinery.setDateOfPurchase(first(facade.getDateOfPurchase(), LocalDate.now().minusYears(++i % 10)));
		machinery.setDepartmentTitle(first(facade.getDepartmentTitle(), () -> createDepartment().getTitle()));
		machinery.setHolderName(first(
			facade.getHolderName(),
			() -> createEmployee(
				Employee.builder()
					.departmentTitle(machinery.getDepartmentTitle())
					.build()
			).getFullName()
		));
		return machinery;
	}

	public Department createDepartment(Department facade) {
		return departmentRepository.insert(buildDepartment(facade));
	}

	public Department createDepartment() {
		return createDepartment(new Department());
	}

	public EmployeeType createEmployeeType(EmployeeType facade) {
		return employeeTypeRepository.insert(buildEmployeeType(facade));
	}

	public EmployeeType createEmployeeType() {
		return createEmployeeType(new EmployeeType());
	}

	public EmployeeRequirement createEmployeeRequirement(EmployeeRequirement facade) {
		return employeeRequirementRepository.insert(buildEmployeeRequirement(facade));
	}

	public EmployeeRequirement createEmployeeRequirement() {
		return createEmployeeRequirement(new EmployeeRequirement());
	}

	public Employee createEmployee(Employee facade) {
		return employeeRepository.insert(buildEmployee(facade));
	}

	public Employee createEmployee() {
		return createEmployee(new Employee());
	}

	public MachineryType createMachineryType(MachineryType facade) {
		return machineryTypeRepository.insert(buildMachineryType(facade));
	}

	public MachineryType createMachineryType() {
		return createMachineryType(new MachineryType());
	}

	public MachineryRequirement createMachineryRequirement(MachineryRequirement facade) {
		return machineryRequirementRepository.insert(buildMachineryRequirement(facade));
	}

	public MachineryRequirement createMachineryRequirement() {
		return createMachineryRequirement(new MachineryRequirement());
	}

	public Machinery createMachinery(Machinery facade) {
		return machineryRepository.insert(buildMachinery(facade));
	}

	public Machinery createMachinery() {
		return createMachinery(new Machinery());
	}
}
