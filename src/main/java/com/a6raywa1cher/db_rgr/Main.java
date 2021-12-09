package com.a6raywa1cher.db_rgr;

import com.a6raywa1cher.db_rgr.config.Config;
import com.a6raywa1cher.db_rgr.config.ConfigLoader;
import com.a6raywa1cher.db_rgr.dblib.DatabaseConnector;
import com.a6raywa1cher.db_rgr.lib.ResourcesUtils;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConfigLoader configLoader = new ConfigLoader(ResourcesUtils.getPathOfResource("config.yml"));
        Config config = configLoader.getConfig();
        System.out.println(config.toString());
        Config.Db db = config.getDb();
        try (DatabaseConnector databaseConnector = new DatabaseConnector(
                db.getJdbc(), db.getUser(), db.getPassword())) {
            String databaseName = databaseConnector.executeSelectSingle("SELECT current_database()", String.class);
            System.out.println("Connected to database " + databaseName);
        }
    }
}
