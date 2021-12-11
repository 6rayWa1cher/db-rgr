package com.a6raywa1cher.db_rgr.model.repository;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.EntityManager;
import com.a6raywa1cher.db_rgr.model.Machinery;

public class MachineryRepository extends CrudRepository<Machinery> {
	public MachineryRepository(EntityManager em) {
		super(Machinery.class, em);
	}
}
