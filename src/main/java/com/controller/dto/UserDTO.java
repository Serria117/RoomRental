/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller.dto;

/**
 *
 * @author hadt2
 */
public class UserDTO {

    private int id;
    private String username;
    private String phone;
    private String password;
    private int authority;
    private String autDescription;
    private int status;
    private String sttDescription;

    public UserDTO(int id, String username, String phone, String password, int authority, String autDescription, int status, String sttDescription) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.authority = authority;
        this.autDescription = autDescription;
        this.status = status;
        this.sttDescription = sttDescription;
    }

    public UserDTO() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "id=" + id + ", userName=" + username + ", password=" + password + ", authority=" + authority + ", status=" + status + '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
        this.autDescription = authority == 1 ? "Admin" : "Nhân viên";
    }

    public int getStatus() {
        return status;
    }

    public String getAutDescription() {
        return autDescription;
    }

    public String getSttDescription() {
        return sttDescription;
    }

    public void setStatus(int status) {
        this.status = status;
        this.sttDescription = status == 1 ? "Đang hoạt động" : "Ngừng hoạt động";
    }

}
