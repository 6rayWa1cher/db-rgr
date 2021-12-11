package com.a6raywa1cher.db_rgr.model.repository;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.model.MachineryRequirement;

public class MachineryRequirementRepository extends CrudRepository<MachineryRequirement> {
	public MachineryRequirementRepository(EntityManager em) {
		super(MachineryRequirement.class, em);
	}
}
