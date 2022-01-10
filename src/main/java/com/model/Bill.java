/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model;

import java.util.Date;

/**
 *
 * @author hadt2
 */
public class Bill {

    private int id;
    private String billNumber;
    private int contractId;
    private String description;
    private int roomPrice;
    private int rentalQuantity;
    private Date createdDate;
    private Date updatedDate;
    private int userId;
    private int status;

    public Bill() {
    }

    public Bill(int id, String billNumber, int contractId, String description, int rentalPrice, int rentalQuantity, Date createdDate, Date updatedDate, int userId, int status) {
        this.id = id;
        this.billNumber = billNumber;
        this.contractId = contractId;
        this.description = description;
        this.roomPrice = rentalPrice;
        this.rentalQuantity = rentalQuantity;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.userId = userId;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Bill{" + "id=" + id + ", billNumber=" + billNumber + ", contractId=" + contractId + ", description=" + description + ", rentalPrice=" + roomPrice + ", rentalQuantity=" + rentalQuantity + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", userId=" + userId + ", status=" + status + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(int roomPrice) {
        this.roomPrice = roomPrice;
    }

    public int getRentalQuantity() {
        return rentalQuantity;
    }

    public void setRentalQuantity(int rentalQuantity) {
        this.rentalQuantity = rentalQuantity;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
