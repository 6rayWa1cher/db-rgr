package com.a6raywa1cher.db_rgr.model.repository;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.DatabaseConnector;
import com.a6raywa1cher.db_rgr.model.Employee;

public class EmployeeRepository extends CrudRepository<Employee> {
	public EmployeeRepository(DatabaseConnector connector) {
		super(Employee.class, connector);
	}
}
