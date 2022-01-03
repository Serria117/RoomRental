package com.model.dao;

import com.model.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class RoomDAO extends Database {

    public List<Room> getAll(boolean allRoom) {
        List<Room> roomList = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {
            conn = this.conn();
            String query = allRoom == true ? "SELECT * FROM ROOM" : "SELECT * FROM ROOM WHERE STATUS = 1";
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            roomList = new ArrayList<>();
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("id"),
                        rs.getString("roomNumber"),
                        rs.getInt("price"),
                        rs.getInt("square"),
                        rs.getString("description"),
                        rs.getInt("status"),
                        rs.getInt("electricCounter"),
                        rs.getInt("waterCounter")
                );
                roomList.add(room);
            }
        } catch (SQLException e) {

        } finally {
            this.closeAll(conn, stm, rs);
        }
        return roomList;
    }

    public List<Room> searchRooms(String key, boolean allRoom) {
        List<Room> result = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stm = null;

        try {
            conn = this.conn();
            result = new ArrayList<>();
            String allCheck = allRoom ? "" : "HAVING status = 1";
            String query = "SELECT * FROM ROOM WHERE roomNumber LIKE CONCAT('%',?,'%') "
                    + "OR description LIKE CONCAT('%',?,'%') "
                    + "OR price <= ? "
                    + allCheck;
            stm = conn.prepareStatement(query);
            int intKey;
            try {
                intKey = Integer.parseInt(key);
            } catch (NumberFormatException e) {
                intKey = 0;
            }
            stm.setString(1, key);
            stm.setString(2, key);
            stm.setInt(3, intKey);
            rs = stm.executeQuery();
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("id"),
                        rs.getString("roomNumber"),
                        rs.getInt("price"),
                        rs.getInt("square"),
                        rs.getString("description"),
                        rs.getInt("status"),
                        rs.getInt("electricCounter"),
                        rs.getInt("waterCounter")
                );
                result.add(room);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return result;
    }

    public Room getRoom(String roomNumber) {
        Room room = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {
            conn = this.conn();
            String query = "SELECT * FROM room WHERE roomNumber = ?";
            stm = conn.prepareStatement(query);
            stm.setString(1, roomNumber);
            rs = stm.executeQuery();
            if (rs.next()) {
                room = new Room(
                        rs.getInt("id"),
                        rs.getString("roomNumber"),
                        rs.getInt("price"),
                        rs.getInt("square"),
                        rs.getString("description"),
                        rs.getInt("status"),
                        rs.getInt("electricCounter"),
                        rs.getInt("waterCounter")
                );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return room;
    }

    //Kiểm tra số phòng trùng khi tạo mới
    public boolean isDuplicateRoom(String roomNumber) {
        boolean check = false;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {
            conn = this.conn();
            String query = "SELECT roomNumber FROM room WHERE roomNumber = ?";
            stm = conn.prepareStatement(query);
            stm.setString(1, roomNumber);
            rs = stm.executeQuery();
            check = rs.isBeforeFirst();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return check;
    }

    public boolean addRoom(Room room) {
        if (isDuplicateRoom(room.getRoomNumber())) {
            JOptionPane.showMessageDialog(null, "Số phòng đã có trong hệ thống.", "Cảnh báo", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        boolean status = false;
        Connection conn = null;
        PreparedStatement stm = null;
        try {
            conn = this.conn();
            String query = "INSERT INTO room SET roomNumber = ?, price = ?, square = ?, description = ?, electricCounter = ?, waterCounter = ?, status = 1";
            stm = conn.prepareStatement(query);
            stm.setString(1, room.getRoomNumber());
            stm.setInt(2, room.getPrice());
            stm.setInt(3, room.getSquare());
            stm.setString(4, room.getDescription());
            stm.setInt(5, 0);
            stm.setInt(6, 0);
            int res = stm.executeUpdate();
            status = res > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            this.closeAll(conn, stm, null);
        }
        return status;
    }

    //Update room info:
    public boolean updateRoom(Room room) {
        boolean status = false;
        Connection conn = null;
        PreparedStatement stm = null;
        try {
            conn = this.conn();
            String query = "UPDATE ROOM SET price = ?, square = ?, "
                    + "description = ?, electricCounter = ?, "
                    + "waterCounter = ? "
                    + "WHERE roomNumber = ?";
            stm = conn.prepareStatement(query);
            stm.setInt(1, room.getPrice());
            stm.setInt(2, room.getSquare());
            stm.setString(3, room.getDescription());
            stm.setInt(4, room.getElectricCounter());
            stm.setInt(5, room.getWaterCounter());
            stm.setString(6, room.getRoomNumber());
            int res = stm.executeUpdate();
            status = res > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            this.closeAll(conn, stm, null);
        }
        return status;
    }

    //test:
    public static void main(String[] args) {
        RoomDAO rdao = new RoomDAO();
        List<Room> rList;
        rList = rdao.searchRooms("phòng khách", true);
        rList.stream().forEach(r -> System.out.println(r));

    }

}
