package com.a6raywa1cher.db_rgr.model.repository;

import com.a6raywa1cher.db_rgr.dblib.CrudRepository;
import com.a6raywa1cher.db_rgr.dblib.DatabaseConnector;
import com.a6raywa1cher.db_rgr.model.MachineryType;

public class MachineryTypeRepository extends CrudRepository<MachineryType> {
	public MachineryTypeRepository(DatabaseConnector connector) {
		super(MachineryType.class, connector);
	}
}
