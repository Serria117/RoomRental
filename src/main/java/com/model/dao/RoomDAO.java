/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model.dao;

import com.model.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class RoomDAO {

    public static Database db = new Database();

    public List<Room> getAll(boolean allRoom) {
        List<Room> roomList = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {
            conn = db.conn();
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
            db.closeAll(conn, stm, rs);
        }
        return roomList;
    }

//    public List<Room> searchRooms(String key) {
//
//    }
    public Room getRoom(int id) {
        Room room = new Room();

        return room;
    }

    //Kiểm tra số phòng trùng khi tạo mới
    public boolean isDuplicateRoom(String roomNumber) {
        boolean check = false;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {
            conn = db.conn();
            String query = "SELECT roomNumber FROM room WHERE roomNumber = ?";
            stm = conn.prepareStatement(query);
            stm.setString(1, roomNumber);
            rs = stm.executeQuery();
            check = rs.isBeforeFirst();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            db.closeAll(conn, stm, rs);
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
            conn = db.conn();
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
            db.closeAll(conn, stm, null);
        }
        return status;
    }

    public boolean updateRoom(int id) {
        boolean status = false;

        return status;

    }

    //test:
    public static void main(String[] args) {

    }

}
