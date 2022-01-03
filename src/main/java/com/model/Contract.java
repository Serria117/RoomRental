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
public class Contract {

    private int id;
    private int roomId;
    private String contractNumber;
    private int price;
    private Date createdDate;
    private Date updatedDate;
    private int userId;
    private int status;
    private String fileLocation;

    @Override
    public String toString() {
        return "Contract{" + "id=" + id + ", roomId=" + roomId + ", contractNumber=" + contractNumber + ", price=" + price + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", userId=" + userId + ", status=" + status + ", fileLocation=" + fileLocation + '}';
    }

    public Contract(int id, int roomId, String contractNumber, int price, Date createdDate, Date updatedDate, int userId, int status, String fileLocation) {
        this.id = id;
        this.roomId = roomId;
        this.contractNumber = contractNumber;
        this.price = price;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.userId = userId;
        this.status = status;
        this.fileLocation = fileLocation;
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

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public Contract() {
    }

}
