/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model;

/**
 *
 * @author hadt2
 */
public class User {

    private int id;
    private String username;
    private String password;
    private int authority;
    private int status;

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", authority=" + authority + ", status=" + status + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public User() {
    }

    public User(int id, String username, String password, int authority, int status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authority = authority;
        this.status = status;
    }

}
