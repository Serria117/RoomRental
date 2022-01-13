/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model.dao;

import com.model.Bill;
import com.model.BillDetail;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author hadt2
 */
public class BillDAO extends DBAccess {

    public List<Bill> getBillByRoom(int RoomId) {
        List<Bill> bList = null;
        try {
            conn = this.conn();
            stm = conn.prepareStatement(
                    "SELECT b.id as 'billId', b.billNumber, r.roomNumber, c.contractNumber, b.createdDate, b.updatedDate, b.total, b.status as 'billStatus' "
                    + "FROM bill as b "
                    + "INNER JOIN contract as c "
                    + "INNER JOIN room as r "
                    + "ON b.contractId = c.id AND c.roomId = r.id "
                    + "WHERE r.id = ?"
            );
            stm.setInt(1, RoomId);
            rs = stm.executeQuery();
            bList = new ArrayList<>();
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setBillNumber(rs.getString("billNumber"));
                bill.setStatus(rs.getInt("billStatus"));
                bill.setTotal(rs.getLong("total"));
                bill.setCreatedDate(rs.getDate("createdDate"));
                bill.setUpdatedDate(rs.getDate("updatedDate"));
                bList.add(bill);
            }
        } catch (SQLException e) {
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return bList;
    }

    public Bill getBillbyNumber(String billNo) {
        Bill bill = null;
        try {
            conn = this.conn();
            stm = conn.prepareStatement("SELECT * FROM bill WHERE billNumber = ?");
            stm.setString(1, billNo);
            rs = stm.executeQuery();
            if (rs.next()) {
                bill = new Bill();
                bill.setBillNumber(billNo);
                bill.setRoomPrice(rs.getInt("roomPrice"));
                bill.setRentalQuantity(rs.getInt("rentalQuantity"));
                bill.setTotal(rs.getInt("total"));
                bill.setCreatedDate(rs.getDate("createdDate"));
                bill.setUpdatedDate(rs.getDate("updatedDate"));
            }
        } catch (SQLException e) {
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return bill;
    }

    public List<BillDetail> getBillDetailByNumber(String billNo) {
        List<BillDetail> bd = null;
        try {
            conn = this.conn();
            stm = conn.prepareStatement(
                    "SELECT b.id, b.roomPrice, b.rentalQuantity, s.serviceName, bd.price, bd.quantity "
                    + "FROM `bill` as b "
                    + "INNER JOIN billdetail as bd "
                    + "INNER JOIN service as s "
                    + "ON b.id = bd.billId AND bd.serviceId = s.id "
                    + "WHERE b.billNumber = ? ORDER BY s.type"
            );
            stm.setString(1, billNo);
            rs = stm.executeQuery();
            bd = new ArrayList<>();
            while (rs.next()) {
                BillDetail b = new BillDetail(
                        rs.getInt("id"),
                        rs.getString("serviceName"),
                        rs.getInt("price"),
                        rs.getInt("quantity")
                );
                bd.add(b);
            }
        } catch (SQLException e) {
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return bd;
    }

    public int addBill(Bill bill) {
        int billId = 0;
        try {
            conn = this.conn();
            String[] col = {"id"};
            stm = conn.prepareStatement("INSERT INTO bill SET "
                    + "billNumber = ?, "
                    + "contractId = ?, "
                    + "roomPrice = ?, "
                    + "rentalQuantity = ?, "
                    + "userId = ?", col);
            stm.setString(1, bill.getBillNumber());
            stm.setInt(2, bill.getContractId());
            stm.setInt(3, bill.getRoomPrice());
            stm.setInt(4, bill.getRentalQuantity());
            stm.setInt(5, bill.getUserId());
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                billId = rs.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Create bill failed: " + e.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return billId;
    }

    public boolean updateBillDetail(int billId, int serviceId, int price, int quantity) {
        boolean result = false;
        try {
            conn = this.conn();
            String query = "INSERT INTO billdetail SET "
                    + "billId = ?, "
                    + "serviceId = ?, "
                    + "price = ?, "
                    + "quantity = ?";
            stm = conn.prepareStatement(query);
            stm.setInt(1, billId);
            stm.setInt(2, serviceId);
            stm.setInt(3, price);
            stm.setInt(4, quantity);
            result = stm.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Update bill_detail failed: " + e.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return result;
    }

    public boolean isDuplicateBill(String billNumber) {
        boolean check = false;
        try {
            conn = this.conn();
            stm = conn.prepareStatement("SELECT billNumber FROM bill WHERE billNumber = ?");
            stm.setString(1, billNumber);
            rs = stm.executeQuery();
            check = rs.isBeforeFirst();
        } catch (SQLException e) {
        }
        return check;
    }
}
