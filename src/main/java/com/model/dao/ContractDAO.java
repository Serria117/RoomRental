/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model.dao;

import com.model.Contract;
import com.model.Guest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author hadt2
 */
public class ContractDAO extends DBAccess {

    public Contract getCurrentContract(int RoomId) {
        Contract contract = null;
        try {
            conn = this.conn();
            stm = conn.prepareStatement("SELECT * FROM contract where RoomId = ? AND status = 1");
            stm.setInt(1, RoomId);
            rs = stm.executeQuery();
            if (rs.next()) {
                contract = new Contract();
                contract.setId(rs.getInt("id"));
                contract.setRoomId(RoomId);
                contract.setPrice(rs.getInt("price"));
                contract.setContractNumber(rs.getString("contractNumber"));
                contract.setCreatedDate(rs.getDate("createdDate"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return contract;
    }

    public int getCurrentContractInRoom(int roomId) {
        int contractId = 0;
        try {
            conn = this.conn();
            stm = conn.prepareStatement("SELECT * FROM contract WHERE roomID = ? AND status = 1");
            stm.setInt(1, roomId);
            rs = stm.executeQuery();
            if (rs.next()) {
                contractId = rs.getInt("id");
            }
        } catch (SQLException e) {
        }
        return contractId;
    }

    public Contract getContract(int id) {
        Contract contract = null;
        try {
            conn = this.conn();
            String query = "SELECT * FROM contract WHERE id = ";
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

    public List<Contract> getContractsOfRoom(int RoomId) {
        List<Contract> listContract = null;
        try {
            conn = this.conn();
            String query = "SELECT * FROM contract WHERE roomId = ? ORDER BY createdDate DESC";
            stm = conn.prepareStatement(query);
            stm.setInt(1, RoomId);
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

    public List<Contract> getAllContract() {
        List<Contract> listContract = null;
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

    public boolean updateContract(int id, int status) {
        boolean check = false;
        try {
            conn = this.conn();
            String query = "UPDATE contract "
                    + "SET status = ? WHERE id = ?";
            stm = conn.prepareStatement(query);
            stm.setInt(1, status);
            stm.setInt(2, id);
            check = stm.executeUpdate() > 0;
        } catch (SQLException e) {
        } finally {
            this.closeAll(conn, stm, null);
        }
        return check;
    }

    public boolean updateContractDetail(int contractId, int guestId, int status) {
        boolean check = false;
        try {
            conn = this.conn();
            String query = "UPDATE contractdetail "
                    + "SET status = ? WHERE contractId = ? AND guestID = ?";
            stm = conn.prepareStatement(query);
            stm.setInt(1, status);
            stm.setInt(2, contractId);
            stm.setInt(3, guestId);
            check = stm.executeUpdate() > 0;
        } catch (SQLException e) {
        } finally {
            this.closeAll(conn, stm, null);
        }
        return check;
    }

    public static void main(String[] args) {
        ContractDAO cdao = new ContractDAO();
        List<Contract> ct = cdao.getAllContract();
        ct.stream().forEach(c -> System.out.println(c));
    }
}
