/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.dto;

import java.text.NumberFormat;

/**
 *
 * @author hadt2
 */
public class BillDTO {

    private int id;
    private String billNumber;
    private int contractId;
    private int rentalQuantity;
    private String total;
    private int roomPrice;
    private String createdDate;
    private String updatedDate;
    private int userId;
    private String sttDescription;
    private int status;

    public int getRentalQuantity() {
        return rentalQuantity;
    }

    public void setRentalQuantity(int rentalQuantity) {
        this.rentalQuantity = rentalQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = NumberFormat.getInstance().format(total);
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

    public int getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(int roomPrice) {
        this.roomPrice = roomPrice;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSttDescription() {
        return sttDescription;
    }

    public void setSttDescription(String sttDescription) {
        this.sttDescription = sttDescription;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        this.setSttDescription(status == 1 ? "Đã thanh toán" : "Chưa thanh toán");
    }

}
