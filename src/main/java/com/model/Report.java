/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model;

import java.util.Date;

/**
 *
 * @author Quan Le
 */
public class Report {

    float total;
    String roomNumber;
    Date createdDate;
    Date updatedDate;

    @Override
    public String toString() {
        return "Report{" + "total=" + total + ", roomNumber=" + roomNumber + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + '}';
    }

    public Report() {
    }

    public Report(float total, String roomNumber, Date createdDate, Date updatedDate) {
        this.total = total;
        this.roomNumber = roomNumber;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

}
