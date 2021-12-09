package com.a6raywa1cher.db_rgr.model.repository;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.DatabaseConnector;
import com.a6raywa1cher.db_rgr.model.EmployeeType;

public class EmployeeTypeRepository extends CrudRepository<EmployeeType> {
	public EmployeeTypeRepository(DatabaseConnector connector) {
		super(EmployeeType.class, connector);
	}
}
