/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model.dao;

import com.model.Bill;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author hadt2
 */
public class BillDAO extends DBAccess {

    public int addBill(Bill bill) {
        int billId = 0;
        try {
            conn = this.conn();
            String[] col = {"id"};
            stm = conn.prepareStatement("INSERT INTO bill SET "
                    + "billNumber = ?, "
                    + "contractId = ?, "
                    + "description = ?, "
                    + "roomPrice = ?, "
                    + "rentalQuantity = ?, "
                    + "userId = ?", col);
            stm.setString(1, bill.getBillNumber());
            stm.setInt(2, bill.getContractId());
            stm.setString(3, bill.getDescription());
            stm.setInt(4, bill.getRoomPrice());
            stm.setInt(5, bill.getRentalQuantity());
            stm.setInt(6, bill.getUserId());
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
