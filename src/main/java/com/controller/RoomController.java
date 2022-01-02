/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

import com.model.Room;
import com.model.dao.RoomDAO;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author hadt2
 */
public class RoomController {

    static RoomDAO roomDAO = new RoomDAO();

    public static List<RoomDTO> displayRooms(boolean allRoom) {
        return roomDAO.getAll(allRoom).stream()
                .map(r -> RoomModelToDTO(r))
                .collect(Collectors.toList());
    }

//    public static RoomDTO searchRoom(String key) {
//
//    }
    public static RoomDTO getRoom(String roomNumber) {
        return RoomModelToDTO(roomDAO.getRoom(roomNumber));
    }

    public static boolean addRoomObj(String roomNumber, String price, String square, String description) {
        Room r = new Room();

        r.setRoomNumber(roomNumber);
        r.setPrice(Integer.parseInt(price));
        r.setSquare(Integer.parseInt(square));
        r.setDescription(description);

        return roomDAO.addRoom(r);
    }

    public static boolean updateRoomObj(String roomNumber, String price, String square, String description, String elec, String water) {
        Room r = new Room();

        r.setRoomNumber(roomNumber);
        r.setPrice(Integer.parseInt(price));

        r.setSquare(Integer.parseInt(square));
        r.setDescription(description);
        r.setElectricCounter(Integer.parseInt(elec));
        r.setWaterCounter(Integer.parseInt(water));

        return roomDAO.updateRoom(r);
    }

    public static RoomDTO RoomModelToDTO(Room r) {
        if (r == null) {
            return null;
        }
        NumberFormat numFormat = NumberFormat.getInstance();
        RoomDTO rdto = new RoomDTO();
        rdto.setId(r.getId());
        rdto.setRoomNumber(r.getRoomNumber());
        rdto.setPrice(numFormat.format(r.getPrice()));
        rdto.setDescription(r.getDescription());
        rdto.setSquare(String.valueOf(r.getSquare()));

        if (r.getStatus() == 0) {
            rdto.setStatus("Đang cho thuê");
        } else {
            rdto.setStatus("Phòng trống");
        }

        rdto.setElectricCounter(String.valueOf(r.getElectricCounter()));
        rdto.setWaterCounter(String.valueOf(r.getWaterCounter()));

        return rdto;

    }

    public static void main(String[] args) {
        List<RoomDTO> list = displayRooms(true);
        list.forEach(r -> System.out.println(r));
    }
}
