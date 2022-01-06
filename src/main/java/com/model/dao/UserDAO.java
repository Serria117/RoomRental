/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model.dao;

import com.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author hadt2
 */
public class UserDAO extends Database {

    //get user:
    public User authenticate(String username, String hashedPassword) {
        User user = null;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = this.conn();
            String query = "SELECT * FROM user WHERE userName = ? AND password = ? AND status = 1";
            stm = conn.prepareStatement(query);
            stm.setString(1, username);
            stm.setString(2, hashedPassword);
            rs = stm.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("userName"));
                user.setPassword(hashedPassword);
            }
        } catch (SQLException e) {

        } finally {
            this.closeAll(conn, stm, rs);
        }
        return user;
    }

}
