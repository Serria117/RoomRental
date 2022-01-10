/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model.dao;

import com.model.Service;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nguyen Huu Tung
 */
public class ServiceDAO extends DBAccess {

    public List<Service> GetAll() {
        List<Service> serviceslist = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = this.conn();
            String query = "SELECT * FROM SERVICE";
            statement = conn.createStatement();

            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Service service = new Service(
                        resultSet.getInt("id"),
                        resultSet.getString("serviceName"),
                        resultSet.getInt("price"),
                        resultSet.getString("unit"),
                        resultSet.getInt("type")
                );
                serviceslist.add(service);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.closeAll(conn, statement, resultSet);
        }
        return serviceslist;
    }

    public boolean InsertService(Service service) {
        boolean res = false;
        try {
            conn = this.conn();
            String query = "INSERT INTO service(serviceName, price, unit) VALUES (?, ? ,?)";
            stm = conn.prepareCall(query);

            stm.setString(1, service.getServiceName());
            stm.setInt(2, service.getPrice());
            stm.setString(3, service.getUnit());

            res = stm.execute();

        } catch (SQLException ex) {
            Logger.getLogger(ServiceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.closeAll(conn, stm, null);
        }
        return res;
    }

    public void UpdateService(Service service) {
        try {
            conn = this.conn();
            String query = "UPDATE service SET serviceName = ?, price = ?, unit = ? " + " WHERE ID = ?";
            stm = conn.prepareCall(query);

            stm.setString(1, service.getServiceName());
            stm.setInt(2, service.getPrice());
            stm.setString(3, service.getUnit());
            stm.setInt(4, service.getId());

            stm.execute();

        } catch (SQLException ex) {
            Logger.getLogger(ServiceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.closeAll(conn, stm, null);
        }
    }

    public void DeleteService(int id) {
        try {
            conn = this.conn();
            String query = "DELETE FROM SERVICE" + " WHERE ID = ?";
            stm = conn.prepareCall(query);

            stm.setInt(1, id);

            stm.execute();

        } catch (SQLException ex) {
            Logger.getLogger(ServiceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.closeAll(conn, stm, null);
        }
    }

    public List<Service> Search(String serviceName) {
        List<Service> serviceslist = new ArrayList<>();
        try {
            conn = this.conn();
            String query = "SELECT * FROM SERVICE WHERE SERVICE LIKE ?";
            stm = conn.prepareCall(query);
            stm.setString(1, "%" + serviceName + "%");

            rs = stm.executeQuery();

            while (rs.next()) {
                Service service = new Service(
                        rs.getInt("id"),
                        rs.getString("serviceName"),
                        rs.getInt("price"),
                        rs.getString("unit")
                );
                serviceslist.add(service);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.closeAll(conn, stm, rs);
        }
        return serviceslist;
    }
}
