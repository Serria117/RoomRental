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
public class ContractDTO {
    private int id;
    private int RoomId;
    private int ContractCode;
    private int Price;
    private Date CreatedDate;
    private Date UpdatedDate;

    public ContractDTO() {
    }

    public ContractDTO(int id, int RoomId, int ContractCode, int Price, Date CreatedDate, Date UpdatedDate) {
        this.id = id;
        this.RoomId = RoomId;
        this.ContractCode = ContractCode;
        this.Price = Price;
        this.CreatedDate = CreatedDate;
        this.UpdatedDate = UpdatedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int RoomId) {
        this.RoomId = RoomId;
    }

    public int getContractCode() {
        return ContractCode;
    }

    public void setContractCode(int ContractCode) {
        this.ContractCode = ContractCode;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int Price) {
        this.Price = Price;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public Date getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(Date UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    @Override
    public String toString() {
        return "ContractDTO{" + "id=" + id + ", RoomId=" + RoomId + ", ContractCode=" + ContractCode + ", Price=" + Price + ", CreatedDate=" + CreatedDate + ", UpdatedDate=" + UpdatedDate + '}';
    }
    
}
