/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.dto;

/**
 *
 * @author hadt2
 */
public class GuestDTO {

    private int id;
    private String fullName;
    private String phone;
    private String dateOfBirth;
    private String status;
    private String citizenId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status == 1 ? "Đang thuê" : "Đã trả phòng";
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public GuestDTO(int id, String fullName, String phone, String dateOfBirth, String status, String citizenId) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        this.citizenId = citizenId;
    }

    public GuestDTO() {
    }

    @Override
    public String toString() {
        return "GuestDTO{" + "id=" + id + ", fullName=" + fullName + ", phone=" + phone + ", dateOfBirth=" + dateOfBirth + ", status=" + status + ", citizenId=" + citizenId + '}';
    }

}
