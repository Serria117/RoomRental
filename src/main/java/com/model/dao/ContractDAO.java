/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model.dao;

import com.model.Contract;
import com.model.Guest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author hadt2
 */
public class ContractDAO extends Database {

    public Contract getContract(int id) {
        Contract contract = null;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = this.conn();
            String query = "SELECT * FROM contract WHER id = ";
            stm = conn.prepareStatement(query);
            stm.setInt(1, id);
            rs = stm.executeQuery();
            if (rs.next()) {
                contract = new Contract();
                contract.setId(rs.getInt("id"));
                contract.setContractNumber(rs.getString("contractNumber"));
                contract.setPrice(rs.getInt("price"));
                contract.setRoomId(rs.getInt("roomId"));
                contract.setCreatedDate(rs.getDate("createdDate"));
                contract.setUpdatedDate(rs.getDate("updatedDate"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection failed");
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return contract;
    }

    public List<Contract> getAllContract() {
        List<Contract> listContract = null;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = this.conn();
            String query = "SELECT * FROM contract";
            stm = conn.prepareStatement(query);
            rs = stm.executeQuery();
            listContract = new ArrayList<>();
            while (rs.next()) {
                Contract contract = new Contract();
                contract.setId(rs.getInt("id"));
                contract.setContractNumber(rs.getString("contractNumber"));
                contract.setPrice(rs.getInt("price"));
                contract.setRoomId(rs.getInt("roomId"));
                contract.setCreatedDate(rs.getDate("createdDate"));
                contract.setUpdatedDate(rs.getDate("updatedDate"));
                contract.setFileLocation(rs.getString("fileLocation"));
                contract.setUserId(rs.getInt("userId"));
                contract.setStatus(rs.getInt("status"));

                listContract.add(contract);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection failed");
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return listContract;
    }

    public int addContract(Contract c) {
        int contractId = 0;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = this.conn();
            String query = "INSERT INTO contract SET "
                    + "contractNumber = ?, "
                    + "roomId = ?, "
                    + "price = ?, "
                    + "userId = ?, "
                    + "status = 1";
            stm = conn.prepareStatement(query, 1);
            stm.setString(1, c.getContractNumber());
            stm.setInt(2, c.getRoomId());
            stm.setInt(3, c.getPrice());
            stm.setInt(4, c.getUserId());
            stm.execute();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                contractId = rs.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Add Contract Error: " + e.getMessage());
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return contractId;
    }

    public boolean bindContractDetail(Contract c, Guest g) {
        //The guest must be inserted into the database first to generate an incremental ID
        boolean res = false;
        Connection conn = null;
        PreparedStatement stm = null;

        try {
            conn = this.conn();
            String query = "INSERT INTO contractdetail "
                    + "SET contractId = ?, guestId = ?, role = ?";
            stm = conn.prepareStatement(query);
            stm.setInt(1, c.getId());
            stm.setInt(2, g.getId());
            stm.setInt(3, g.getRole());
            res = stm.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Binding Contract Error: " + e.getMessage());
        } finally {
            this.closeAll(conn, stm, null);
        }

        return res;
    }

    public boolean updateContract(Contract c) {
        boolean rs = false;
        Connection conn = null;
        PreparedStatement stm = null;
        try {
            conn = this.conn();
            String query = "UPDATE contact "
                    + "SET status = ? WHERE id = ?";
            stm = conn.prepareStatement(query);
            rs = stm.executeUpdate() > 0;
        } catch (SQLException e) {
        } finally {
            this.closeAll(conn, stm, null);
        }
        return rs;
    }

    public static void main(String[] args) {
        ContractDAO cdao = new ContractDAO();
        List<Contract> ct = cdao.getAllContract();
        ct.stream().forEach(c -> System.out.println(c));
    }
}
