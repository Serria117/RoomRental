/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

import com.controller.dto.GuestDTO;
import com.model.Guest;
import com.model.dao.GuestDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author hadt2
 */
public class GuestController {

    static GuestDAO guestDAO = new GuestDAO();

    public List<GuestDTO> displaySearchGuest(String key) {
        return guestDAO.searchGuest(key).stream().map(g -> guestModelToDTO(g)).collect(Collectors.toList());
    }

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

    public List<GuestDTO> getGuestsByContract(int ContractId) {
        return guestDAO.getGuestsByContract(ContractId).stream()
                .map(g -> guestModelToDTO(g))
                .collect(Collectors.toList());
    }

    public int addGuest(Guest g) {
        return guestDAO.addGuest(g); //Return the added guest's id
    }

    public boolean updateGuestStatus(int id, int status) {
        return guestDAO.updateGuestStatus(id, status);
    }

    public Guest searchExistGuest(String cId) {
        return guestDAO.searchExactCitizenID(cId);
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
            gdto.setStatus(g.getStatus());
        }
        return gdto;
    }

    public List<GuestDTO> getAll() {
        return guestDAO.getAll("all").stream()
                .map(g -> guestModelToDTO(g))
                .collect(Collectors.toList());
    }

    public Guest getByCId(String cccd) {
        return guestDAO.getByCId(cccd);
    }

    public void updateGuestInfor(int id, String fullName, String citizenId, String phone, Date dateOfBirth, int status) {
        guestDAO.updateGuestInfor(id, fullName, citizenId, phone, dateOfBirth, status);
    }

    public static void main(String[] args) {
        List<GuestDTO> list = displayCurrentGuestDTO("P101", 1);
        list.forEach(r -> System.out.println(r));
    }
}
