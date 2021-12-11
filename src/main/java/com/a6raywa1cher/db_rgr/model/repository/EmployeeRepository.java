package com.a6raywa1cher.db_rgr.model.repository;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.model.Employee;

public class EmployeeRepository extends CrudRepository<Employee> {
	public EmployeeRepository(EntityManager em) {
		super(Employee.class, em);
	}
}
