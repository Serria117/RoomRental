package com.model;

import java.util.Date;

public class Guest {

    private int id;
    private String fullName;
    private String phone;
    private Date dateOfBirth;
    private int status;
    private String citizenId;
    private int role;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Guest() {
    }

    public Guest(int id, String fullName, String phone, Date dateOfBirth, int status, String citizenId) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        this.citizenId = citizenId;
    }

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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    @Override
    public String toString() {
        return "Guest{" + "id=" + id + ", fullName=" + fullName + ", phone=" + phone + ", dteOfBirth=" + dateOfBirth + ", status=" + status + ", citizenId=" + citizenId + '}';
    }

}
