package com.a6raywa1cher.db_rgr.model.repository;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.model.EmployeeRequirement;

public class EmployeeRequirementRepository extends CrudRepository<EmployeeRequirement> {
	public EmployeeRequirementRepository(EntityManager em) {
		super(EmployeeRequirement.class, em);
	}
}
