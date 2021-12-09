package com.a6raywa1cher.db_rgr.lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnector implements AutoCloseable {
    private final Connection con;

    public DatabaseConnector() throws SQLException {
        this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/myDb", "user1", "pass");
        con.setAutoCommit(false);
    }

    public PreparedStatement openPS(String sql, Object... params) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            ps.setObject(i, param);
        }
        return ps;
    }

    @Override
    public void close() throws Exception {
        con.close();
    }
}
