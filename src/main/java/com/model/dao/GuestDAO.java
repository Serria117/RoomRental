package com.model.dao;

import com.model.Guest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {

    public static Database db = new Database();

    public List<Guest> getAll(boolean allGuest) {
        List<Guest> guestList = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {
            conn = db.conn();
            String query = "SELECT * FROM GUEST";
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
            System.out.println("CAN NOT CONNECT TO DB");
        } finally {
            db.closeAll(conn, stm, rs);
        }
        return guestList;
    }

    public static void main(String[] args) {
        GuestDAO guest = new GuestDAO();
        List<Guest> list = guest.getAll(true);
        if (list == null) {
            System.out.println("NULL");
        } else {
            list.stream().forEach(g -> System.out.println(g.toString()));
        }
    }
}
