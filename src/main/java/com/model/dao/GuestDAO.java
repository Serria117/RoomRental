package com.model.dao;

import com.model.Guest;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class GuestDAO extends DBAccess {

    public static DBAccess db = new DBAccess();

    public List<Guest> getByRoom(String roomNo, int status) {
        List<Guest> guestList = null;
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

    public int addGuest(Guest g) {
        //This function must return the id of the guest has been inserted into the database
        int guestId = 0;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            conn = this.conn();
            String[] autoCol = {"id"};
            String query = "INSERT INTO guest SET fullName = ?, citizenId = ?, dateOfBirth = ?, phone = ?, status = 1";
            stm = conn.prepareStatement(query, autoCol); //Get the generated value from 'id' column
            stm.setString(1, g.getFullName());
            stm.setString(2, g.getCitizenId());
            stm.setString(3, dateFormat.format(g.getDateOfBirth()));
            stm.setString(4, g.getPhone());
            stm.execute();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                guestId = rs.getInt(1); //get the id of the new guest and return it
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Add guest error: " + e.getMessage());
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return guestId;
    }

    public List<Guest> searchGuest(String key) {
        List<Guest> searchList = null;
        try {
            conn = this.conn();
            String query = "SELECT * FROM guest WHERE fullName LIKE CONCAT('%',?,'%') "
                    + "OR phone LIKE CONCAT('%',?,'%') "
                    + "OR citizenId LIKE CONCAT('%',?,'%')";
            stm = conn.prepareStatement(query);
            stm.setString(1, key);
            stm.setString(2, key);
            stm.setString(3, key);
            rs = stm.executeQuery();
            searchList = new ArrayList<>();
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
                searchList.add(guest);
            }
        } catch (SQLException e) {
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return searchList;
    }

    public static void main(String[] args) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        GuestDAO gdao = new GuestDAO();
        Guest g = new Guest();
        g.setFullName("Hà");
        g.setDateOfBirth(dateFormat.parse("25/06/1987"));
        g.setCitizenId("11111111111");
        g.setPhone("0988144796");
        System.out.println("new Id = " + gdao.addGuest(g));
    }
}
