package com.a6raywa1cher.db_rgr.dblib;

import com.a6raywa1cher.db_rgr.config.Config;
import com.a6raywa1cher.db_rgr.config.ConfigLoader;
import com.a6raywa1cher.db_rgr.dblib.analyzer.ClassAnalyzer;
import com.a6raywa1cher.db_rgr.lib.ResourcesUtils;

public class DbLibInitializer {
	public static EntityManager initialize() throws Exception {
		ConfigLoader configLoader = new ConfigLoader(ResourcesUtils.getPathOfResource("config.yml"));
		Config config = configLoader.getConfig();
		Config.Db db = config.getDb();
		return initialize(db.getJdbc(), db.getUser(), db.getPassword());
	}

	public static EntityManager initialize(String jdbcUrl, String user, String password) throws Exception {
		DatabaseConnector databaseConnector = new DatabaseConnector(jdbcUrl, user, password);
		ClassAnalyzer classAnalyzer = ClassAnalyzer.getInstance();
		EntityParser entityProcessor = new EntityParser(classAnalyzer);
		return new EntityManager(classAnalyzer, entityProcessor, databaseConnector);
	}
}
