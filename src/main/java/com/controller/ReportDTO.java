/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

import java.util.Date;

/**
 *
 * @author Quan Le
 */
public class ReportDTO {
    float total;
    int roomNumber;
    Date Time;

    public ReportDTO() {
    }

    public ReportDTO(float total, int roomId, Date Time) {
        this.total = total;
        this.roomNumber = roomId;
        this.Time = Time;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
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
        return "ReportDTO{" + "total=" + total + ", roomId=" + roomNumber + ", Time=" + Time + '}';
    }
    
    
}
