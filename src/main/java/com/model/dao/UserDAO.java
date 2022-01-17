/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model.dao;

import com.model.User;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author hadt2
 */
public class UserDAO extends DBAccess {

    //login:
    public User authenticate(String username, String hashedPassword) {
        User user = null;
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
                user.setPhone(rs.getString("phone"));
                user.setPassword(hashedPassword);
                user.setAuthority(rs.getInt("authority"));
                user.setStatus(rs.getInt("status"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Database access error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return user;
    }

    //Get user list:
    public List<User> getAllUser() {
        List<User> list = null;
        try {
            conn = this.conn();
            String query = "SELECT * FROM user";
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            list = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("userName"));
                user.setPhone(rs.getString("phone"));
                user.setPassword(rs.getString("password"));
                user.setStatus(rs.getInt("status"));
                user.setAuthority(rs.getInt("authority"));
                list.add(user);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Database access error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return list;
    }

    public User getUserById(int id) {
        User user = null;
        try {
            conn = this.conn();
            String query = "SELECT * FROM user WHERE id = ?";
            stm = conn.prepareStatement(query);
            stm.setInt(1, id);
            rs = stm.executeQuery();
            user = new User();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("userName"));
                user.setPhone(rs.getString("phone"));
                user.setPassword(rs.getString("password"));
                user.setStatus(rs.getInt("status"));
                user.setAuthority(rs.getInt("authority"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Database access error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return user;
    }

    //Check duplicated username:
    public boolean isDuplicatedUser(String userName) {
        boolean result = false;
        try {
            conn = this.conn();
            String query = "SELECT userName FROM user WHERE userName = ?";
            stm = conn.prepareStatement(query);
            stm.setString(1, userName);
            rs = stm.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Database access error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return result;
    }

    //update password:
    public boolean changePassword(int id, String newPass) {
        boolean result = false;
        try {
            conn = this.conn();
            String query = "UPDATE user SET password = ? WHERE id = ?";
            stm = conn.prepareStatement(query);
            stm.setString(1, newPass);
            stm.setInt(2, id);
            result = stm.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Database access error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.closeAll(conn, stm, null);
        }
        return result;
    }

    //Check duplicated phone:
    public boolean isDuplicatePhone(int id, String phone) {
        boolean result = false;
        try {
            conn = this.conn();
            String query = "SELECT phone FROM user WHERE phone = ? AND id = ?";
            stm = conn.prepareStatement(query);
            stm.setString(1, phone);
            stm.setInt(2, id);
            rs = stm.executeQuery();
            if (!rs.next()) {
                result = true;
            }
        } catch (SQLException e) {
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return result;
    }

    //Change phone:
    public boolean changePhone(int id, String newPhone) {
        boolean result = false;
        try {
            conn = this.conn();
            String query = "UPDATE user SET phone = ? WHERE id = ?";
            stm = conn.prepareStatement(query);
            stm.setString(1, newPhone);
            stm.setInt(2, id);
            result = stm.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Database access error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.closeAll(conn, stm, null);
        }
        return result;
    }

    public boolean addNewUser(User user) {
        boolean result = false;
        try {
            conn = this.conn();
            String query = "INSERT INTO user SET "
                    + "userName = ?, "
                    + "password = ?, "
                    + "phone = ?, "
                    + "authority = ?";
            stm = conn.prepareStatement(query);
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getPassword());
            stm.setString(3, user.getPhone());
            stm.setInt(4, user.getAuthority());
            result = stm.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Database access error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.closeAll(conn, stm, null);
        }
        return result;
    }

    public boolean updateUserStatus(int id, int status) {
        boolean check = false;
        try {
            conn = this.conn();
            stm = conn.prepareStatement("UPDATE user SET status = ? WHERE id = ?");
            stm.setInt(1, status);
            stm.setInt(2, id);
            check = stm.executeUpdate() > 0;
        } catch (SQLException e) {
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return check;
    }

}
