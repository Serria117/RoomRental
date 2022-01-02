/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

/**
 *
 * @author hadt2
 */
public class GuestDTO {

    private int id;
    private String fullName;
    private String phone;
    private String picture;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public GuestDTO(int id, String fullName, String phone, String picture, String dateOfBirth, String status, String citizenId) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.picture = picture;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        this.citizenId = citizenId;
    }

    public GuestDTO() {
    }

    @Override
    public String toString() {
        return "GuestDTO{" + "id=" + id + ", fullName=" + fullName + ", phone=" + phone + ", picture=" + picture + ", dateOfBirth=" + dateOfBirth + ", status=" + status + ", citizenId=" + citizenId + '}';
    }

}
