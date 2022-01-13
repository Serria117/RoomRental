/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model;

/**
 *
 * @author hadt2
 */
public class BillDetail {

    private int billId;
    private int serviceId;
    private String serviceName;
    private int price;
    private int quantity;

    public BillDetail() {
    }

    public BillDetail(int billId, String serviceName, int price, int quantity) {
        this.billId = billId;
        this.serviceName = serviceName;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "BillDetail{" + "billId=" + billId + ", serviceId=" + serviceId + ", serviceName=" + serviceName + ", price=" + price + ", quantity=" + quantity + '}';
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
