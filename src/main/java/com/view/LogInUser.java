/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.view;

/**
 *
 * @author hadt2
 */
public class LogInUser {

    protected int id;
    protected String userName;
    protected int authority;
    protected int status;

    public LogInUser(int id, String userName, int authority, int status) {
        this.id = id;
        this.userName = userName;
        this.authority = authority;
        this.status = status;
    }

    public LogInUser() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
