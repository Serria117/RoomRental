/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

/**
 *
 * @author hadt2
 */
public class ContractDTO {

    private int id;
    private int roomId;
    private String contractNumber;
    private String price;
    private String createdDate;
    private String updatedDate;

    @Override
    public String toString() {
        return "ContractDTO{" + "id=" + id + ", roomId=" + roomId + ", contractNumber=" + contractNumber + ", price=" + price + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + '}';
    }

    public ContractDTO() {
    }

    public ContractDTO(int id, int roomId, String contractNumber, String price, String createdDate, String updatedDate) {
        this.id = id;
        this.roomId = roomId;
        this.contractNumber = contractNumber;
        this.price = price;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
