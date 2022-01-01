/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author hadt2
 */
public class Database {

    private final String userName = "root";
    private final String password = "";
    private final String connString = "jdbc:mysql://localhost:3306/ql_chungcu";

    //Tạo kết nối
    public Connection conn() throws SQLException {
        return DriverManager.getConnection(connString, userName, password);
    }

    //Đóng kết nối, statement và result-set
    public void closeAll(Connection conn, Statement stm, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
    }
}
