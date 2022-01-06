package com.model;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Scalar.String;

public class Room {

    private int id;
    private String roomNumber;
    private int price;
    private int square;
    private String description;
    private int status;
    private int electricCounter;
    private int waterCounter;

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", roomNumber=" + roomNumber + ", price=" + price + ", square=" + square + ", description=" + description + ", status=" + status + ", electricCounter=" + electricCounter + ", waterCounter=" + waterCounter + '}';
    }

    public Room() {
    }

    public Room(int id, String roomNumber, int price, int square, String description, int status, int electricCounter, int waterCounter) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
        this.square = square;
        this.description = description;
        this.status = status;
        this.electricCounter = electricCounter;
        this.waterCounter = waterCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSquare() {
        return square;
    }

    public void setSquare(int square) {
        this.square = square;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getElectricCounter() {
        return electricCounter;
    }

    public void setElectricCounter(int electricCounter) {
        this.electricCounter = electricCounter;
    }

    public int getWaterCounter() {
        return waterCounter;
    }

    public void setWaterCounter(int waterCounter) {
        this.waterCounter = waterCounter;
    }

}
