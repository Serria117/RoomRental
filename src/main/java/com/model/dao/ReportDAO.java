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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Quan Le
 */
public class ReportDAO {

    public static DBAccess db = new DBAccess();

    // tổng các phòng đã thanh toán
    public List<Report> payment() {
        LocalDate curent = LocalDate.now();
        int m = curent.getMonthValue();
        String month = String.valueOf(m);
        int y = curent.getYear();
        String year = String.valueOf(y);
        List<Report> contList = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stm = null;
        try {
            conn = db.conn();
            // sửa chỗ này theo DB
            String query = "SELECT r.roomNumber as sophong, b.total as total, b.updatedDate FROM `bill` b INNER JOIN `contract` c on b.contractId = c.Id INNER JOIN `room` r on c.roomId = r.id  where b.updatedDate BETWEEN CONCAT(?,'-',?,'-','1',' 00:00:00') and CONCAT(?,'-',?,'-','31',' 23:59:59') and  b.status = 1";
            stm = conn.prepareStatement(query);
            stm.setString(1, year);
            stm.setString(2, month);
            stm.setString(3, year);
            stm.setString(4, month);
            rs = stm.executeQuery();
            contList = new ArrayList<>();
            while (rs.next()) {
                Report rp = new Report();
                rp.setTotal(rs.getInt("total"));
                rp.setRoomNumber(rs.getString("sophong"));
                rp.setUpdatedDate(rs.getDate("updatedDate"));
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
            String query = "SELECT r.roomNumber as sophong, b.total as total FROM `bill` b INNER JOIN `contract` c on b.contractId = c.Id INNER JOIN `room` r on c.roomId = r.id  where b.status = 0 order by r.roomNumber";
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
        ReportDAO rdao = new ReportDAO();
        List<Report> rp;
        rp = rdao.payment();
        rp.stream().forEach(r -> System.out.println(r.toString()));
    }
}
