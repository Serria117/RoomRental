package com.model.dao;

import com.model.Guest;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class GuestDAO extends DBAccess {

    public static DBAccess db = new DBAccess();

    public List<Guest> getByRoom(String roomNo, int status) {
        List<Guest> guestList = null;
        try {
            conn = this.conn();
            String query = "SELECT g.id, g.`fullName`, g.phone, g.`dateOfBirth`, g.status, g.`citizenId` "
                    + "FROM contract AS ct "
                    + "INNER JOIN contractdetail AS cd "
                    + "INNER JOIN guest AS g "
                    + "INNER JOIN room AS r "
                    + "WHERE ct.id = cd.contractId AND g.id = cd.guestId "
                    + "AND r.id = ct.`roomId` "
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
        Guest existGuest = searchExactCitizenID(g.getCitizenId());
        if (existGuest != null) {
            guestId = existGuest.getId();
        } else {
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
        }
        return guestId;
    }

    public Guest searchExactCitizenID(String cId) {
        Guest guest = null;
        try {
            conn = this.conn();
            stm = conn.prepareStatement("SELECT * FROM guest WHERE citizenId = ?");
            stm.setString(1, cId);
            rs = stm.executeQuery();
            if (rs.next()) {
                guest = new Guest();
                guest.setFullName(rs.getString("fullName"));
                guest.setPhone(rs.getString("phone"));
                guest.setId(rs.getInt("id"));
                guest.setDateOfBirth(rs.getDate("dateOfBirth"));
            }
        } catch (SQLException e) {
        }
        return guest;
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

    public boolean updateGuestStatus(int id, int status) {
        boolean check = false;
        try {
            conn = this.conn();
            stm = conn.prepareStatement("UPDATE guest SET status = ? WHERE id = ?");
            stm.setInt(1, status);
            stm.setInt(2, id);
            check = stm.executeUpdate() > 0;
        } catch (SQLException e) {
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return check;
    }

//    public static void main(String[] args) throws ParseException {
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        GuestDAO gdao = new GuestDAO();
//        Guest g = new Guest();
//        g.setFullName("H??");
//        g.setDateOfBirth(dateFormat.parse("25/06/1987"));
//        g.setCitizenId("11111111111");
//        g.setPhone("0988144796");
//        System.out.println("new Id = " + gdao.addGuest(g));
//    }
    public List<Guest> getGuestsByContract(int contractId) {
        List<Guest> listGuest = null;
        try {
            conn = this.conn();
            String query = "SELECT * FROM guest "
                    + "INNER JOIN contractdetail ON guest.id = contractdetail.guestId "
                    + "WHERE contractdetail.contractId = ?;";
            stm = conn.prepareStatement(query);
            stm.setInt(1, contractId);
            rs = stm.executeQuery();
            listGuest = new ArrayList<>();
            while (rs.next()) {
                Guest guest = new Guest(
                        rs.getInt("id"),
                        rs.getString("fullName"),
                        rs.getString("phone"),
                        rs.getDate("dateOfBirth"),
                        rs.getInt("status"),
                        rs.getString("citizenId")
                );
                listGuest.add(guest);
            }
        } catch (SQLException e) {
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return listGuest;
    }

    public Guest getByCId(String cccd) {
        Guest g = null;
        try {
            conn = this.conn();
            String query = "SELECT * FROM guest WHERE citizenId = ?";
            stm = conn.prepareStatement(query);
            stm.setString(1, cccd);
            rs = stm.executeQuery();
            if (rs.next()) {
                g = new Guest(
                        rs.getInt("id"),
                        rs.getString("fullName"),
                        rs.getString("phone"),
                        rs.getDate("dateOfBirth"),
                        rs.getInt("status"),
                        rs.getString("citizenId")
                );
            }
        } catch (SQLException e) {
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return g;
    }

    public void updateGuestInfor(int id, String fullName, String citizenId, String phone, Date dateOfBirth, int status) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            conn = this.conn();
            stm = conn.prepareStatement("UPDATE guest SET fullName = ?, citizenId = ?, phone = ?, dateOfBirth = ? ,status = ? WHERE id = ?");
            stm.setString(1, fullName);
            stm.setString(2, citizenId);
            stm.setString(3, phone);
            stm.setString(4, dateFormat.format(dateOfBirth));
            stm.setInt(5, status);
            stm.setInt(6, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("L???i ??? ch??? update guest");
        } finally {
            this.closeAll(conn, stm, rs);
        }
    }
}
