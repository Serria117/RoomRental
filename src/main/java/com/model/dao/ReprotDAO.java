/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model.dao;

import com.model.Report;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Quan Le
 */
public class ReprotDAO {

    public static DBAccess db = new DBAccess();

    // tổng các phòng đã thanh toán
    public List<Report> payment() {
        List<Report> contList = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {
            conn = db.conn();
            // sửa chỗ này theo DB
            String query = "SELECT r.roomNumber as sophong, b.total as total FROM `bill` b INNER JOIN `contract` c on b.contractId = c.Id INNER JOIN `room` r on c.roomId = r.id  where status = 1";
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            contList = new ArrayList<>();
            while (rs.next()) {
                Report rp = new Report(
                        rs.getFloat("total"),
                        rs.getString("sophong"),
                        rs.getDate("date")
                );
                contList.add(rp);
            }
        } catch (SQLException e) {
            System.out.println("Payment" + e.getMessage());
        } finally {
            db.closeAll(conn, stm, rs);
        }
        return contList;
    }

    public List<Report> notPayment() {
        List<Report> contListNo = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {
            conn = db.conn();
            // sửa chỗ này theo DB
            String query = "SELECT r.roomNumber as sophong, b.total as total FROM `bill` b INNER JOIN `contract` c on b.contractId = c.Id INNER JOIN `room` r on c.roomId = r.id  where b.status = 0";
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            contListNo = new ArrayList<>();
            while (rs.next()) {
                Report rpo = new Report();
                rpo.setRoomNumber(rs.getString("sophong"));
                rpo.setTotal(rs.getFloat("total"));
                contListNo.add(rpo);
            }

        } catch (SQLException e) {
            System.out.println("notPayment error: " + e.getMessage());
        } finally {
            db.closeAll(conn, stm, rs);
        }
        return contListNo;
    }

    public static void main(String[] args) {
        ReprotDAO rdao = new ReprotDAO();
        List<Report> rp = new ArrayList<>();
        rp = rdao.notPayment();
        rp.stream().forEach(r -> System.out.println(r.toString()));
    }
}
