package com.controller;

/**
 *
 * @author hadt2
 */
public class RoomDTO {

    private int id;
    private String roomNumber;
    private String price;
    private String square;
    private String description;
    private String status;
    private String electricCounter;
    private String waterCounter;

    public RoomDTO() {
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSquare() {
        return square;
    }

    public void setSquare(String square) {
        this.square = square;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getElectricCounter() {
        return electricCounter;
    }

    public void setElectricCounter(String electricCounter) {
        this.electricCounter = electricCounter;
    }

    public String getWaterCounter() {
        return waterCounter;
    }

    public void setWaterCounter(String waterCounter) {
        this.waterCounter = waterCounter;
    }

    public RoomDTO(int id, String roomNumber, String price, String square, String description, String status, String electricCounter, String waterCounter) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
        this.square = square;
        this.description = description;
        this.status = status;
        this.electricCounter = electricCounter;
        this.waterCounter = waterCounter;
    }

    @Override
    public String toString() {
        return "RoomDTO{" + "id=" + id + ", roomNumber=" + roomNumber + ", price=" + price + ", square=" + square + ", description=" + description + ", status=" + status + ", electricCounter=" + electricCounter + ", waterCounter=" + waterCounter + '}';
    }

}
