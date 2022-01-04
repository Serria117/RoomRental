/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

import com.model.Guest;
import com.model.dao.GuestDAO;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author hadt2
 */
public class GuestController {

    static GuestDAO guestDAO = new GuestDAO();

    public static List<GuestDTO> displayCurrentGuestDTO(String room, int status) {
        return guestDAO.getByRoom(room, status).stream()
                .map(obj -> guestModelToDTO(obj))
                .collect(Collectors.toList());
    }

    public List<GuestDTO> displaySelectedGuestDTO(List<Guest> guestModelList) {
        return guestModelList.stream()
                .map(obj -> guestModelToDTO(obj))
                .collect(Collectors.toList());
    }

    public int addGuest(Guest g) {
        return guestDAO.addGuest(g); //Return the added guest's id
    }

    public static GuestDTO guestModelToDTO(Guest g) {
        GuestDTO gdto;
        if (g == null) {
            gdto = null;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            gdto = new GuestDTO();
            gdto.setId(g.getId());
            gdto.setFullName(g.getFullName());
            gdto.setDateOfBirth(dateFormat.format(g.getDateOfBirth()));
            gdto.setCitizenId(g.getCitizenId());
            gdto.setPhone(g.getPhone());
        }
        return gdto;
    }

    public static void main(String[] args) {
        List<GuestDTO> list = displayCurrentGuestDTO("P101", 1);
        list.forEach(r -> System.out.println(r));
    }
}
