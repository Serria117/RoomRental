/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.dto;

/**
 *
 * @author Quan Le
 */
public class ReportDTO {

    float total;
    String roomNumber;
    String createdDate; //Fixed
    String updatedDate;

    public ReportDTO() {
    }

    public ReportDTO(float total, String roomNumber, String createdDate, String updatedDate) {
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

}
