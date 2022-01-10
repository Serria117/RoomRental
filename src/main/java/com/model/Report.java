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
    Date Time;

    public Report() {
    }

    public Report(float total, String roomNumber, Date Time) {
        this.total = total;
        this.roomNumber = roomNumber;
        this.Time = Time;
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

    public Date getTime() {
        return Time;
    }

    public void setTime(Date Time) {
        this.Time = Time;
    }

    @Override
    public String toString() {
        return "Report{" + "total=" + total + ", roomId=" + roomNumber + ", Time=" + Time + '}';
    }

}
