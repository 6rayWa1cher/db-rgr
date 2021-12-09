package com.a6raywa1cher.db_rgr.model.repository;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.DatabaseConnector;
import com.a6raywa1cher.db_rgr.model.Department;

public class DepartmentRepository extends CrudRepository<Department> {
	public DepartmentRepository(DatabaseConnector connector) {
		super(Department.class, connector);
	}
}
