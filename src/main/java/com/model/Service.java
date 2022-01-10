/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

/**
 *
 * @author Nguyen Huu Tung
 */
public class Service {

    private int Id;
    private String serviceName;
    private int price;
    private String unit;
    private int type;

    public Service(int Id, String serviceName, int price, String unit, int type) {
        this.Id = Id;
        this.serviceName = serviceName;
        this.price = price;
        this.unit = unit;
        this.type = type;
    }

    public Service(int Id, String serviceName, int price, String unit) {
        this.Id = Id;
        this.serviceName = serviceName;
        this.price = price;
        this.unit = unit;
    }

    public Service(String serviceName, int price, String unit) {
        this.serviceName = serviceName;
        this.price = price;
        this.unit = unit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Service() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
