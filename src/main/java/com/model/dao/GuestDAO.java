package com.model.dao;

import com.model.Guest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class GuestDAO extends Database {

    public static Database db = new Database();

    public List<Guest> getByRoom(String roomNo, int status) {
        List<Guest> guestList = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {
            conn = this.conn();
            String query = "SELECT g.id, g.`fullName`, g.phone, g.picture, g.`dateOfBirth`, g.status, g.`citizenId` "
                    + "FROM contract AS ct "
                    + "INNER JOIN contractdetail AS cd "
                    + "INNER JOIN guest AS g "
                    + "INNER JOIN room AS r "
                    + "WHERE ct.id = cd.contractId AND g.id = cd.guestId AND r.id = ct.`roomId` "
                    + "AND r.`roomNumber` = ? AND g.status = ?";

            stm = conn.prepareStatement(query);
            stm.setString(1, roomNo);
            stm.setInt(2, status);
            rs = stm.executeQuery();
            guestList = new ArrayList<>();
            while (rs.next()) {
                Guest guest = new Guest(
                        rs.getInt("id"),
                        rs.getString("fullName"),
                        rs.getString("phone"),
                        rs.getString("picture"),
                        rs.getDate("dateOfBirth"),
                        rs.getInt("status"),
                        rs.getString("citizenId")
                );
                guestList.add(guest);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection failed." + e.getMessage());
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return guestList;
    }

    public List<Guest> getAll(String key) {
        List<Guest> guestList = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {
            conn = this.conn();
            String query = "SELECT * FROM GUEST WHERE ";
            String param = key.equals("all") ? "TRUE" : (key.equals("active") ? "STATUS = 1" : "STATUS = 0");
            query = query + param;
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            guestList = new ArrayList<>();
            while (rs.next()) {
                Guest guest = new Guest(
                        rs.getInt("id"),
                        rs.getString("fullName"),
                        rs.getString("phone"),
                        rs.getString("picture"),
                        rs.getDate("dateOfBirth"),
                        rs.getInt("status"),
                        rs.getString("citizenId")
                );
                guestList.add(guest);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection failed.");
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return guestList;
    }

    public static void main(String[] args) {
        GuestDAO guest = new GuestDAO();
        List<Guest> list = guest.getByRoom("P101", 1);
        if (list == null) {
            System.out.println("NULL");
        } else {
            list.stream().forEach(g -> System.out.println(g.toString()));
        }
    }
}
