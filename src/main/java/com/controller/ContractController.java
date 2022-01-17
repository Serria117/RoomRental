/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

import com.controller.dto.ContractDTO;
import com.controller.dto.RoomDTO;
import com.model.Contract;
import com.model.Guest;
import com.model.dao.ContractDAO;
import com.model.dao.GuestDAO;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Quan Le
 */
public class ContractController {

    public ContractDAO contractDAO = new ContractDAO();
    public GuestDAO guestDAO = new GuestDAO();

    public String genContractNumber(String roomNo) {
        Calendar today = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return roomNo + "_" + dateFormat.format(today.getTime());
    }

    // lấy toàn bộ dữ liệu
    public List<ContractDTO> getAllContract() {
        return contractDAO.getAllContract().stream()
                .map(r -> ContractModeltoDTO(r))
                .collect(Collectors.toList());
    }

    //Insert:
    public boolean addContract(RoomDTO room, List<Guest> gList, int userId) {
        //Add contract:
        boolean res = false;
        Contract cModel = new Contract();
        cModel.setContractNumber(genContractNumber(room.getRoomNumber()));
        cModel.setRoomId(room.getId());
        cModel.setPrice(Integer.parseInt(room.getPrice().replace(",", "")));
        cModel.setUserId(userId);
        cModel.setId(contractDAO.addContract(cModel)); //set back the id generated from database to obj
        //Bind guest:
        if (cModel.getId() > 0) {
            for (Guest g : gList) {
                //Set the first guest as 'representer'
                if (gList.indexOf(g) == 0) {
                    g.setRole(1);
                }
                int id = guestDAO.addGuest(g);
                g.setId(id);
                contractDAO.bindContractDetail(cModel, g);
                res = true;
            }
        }
        return res;
    }

    public boolean updateContractStatus(int id, int status) {
        return contractDAO.updateContract(id, status);
    }

    public boolean updateContractDetail(int contractId, int guestId, int status) {
        return contractDAO.updateContractDetail(contractId, guestId, status);
    }

    public int getCurrentContract(int RoomId) {
        return contractDAO.getCurrentContractInRoom(RoomId);
    }

    //Convert dữ liệu để hiển thị
    public static ContractDTO ContractModeltoDTO(Contract cModel) {
        if (cModel == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat num = NumberFormat.getInstance();
        ContractDTO cdto = new ContractDTO();
        cdto.setId(cModel.getId());
        cdto.setRoomId(cModel.getRoomId());
        cdto.setPrice(num.format(cModel.getPrice()));
        cdto.setContractNumber(cModel.getContractNumber());
        cdto.setCreatedDate(dateFormat.format(cModel.getCreatedDate()));
        cdto.setUpdatedDate(dateFormat.format(cModel.getUpdatedDate()));
        return cdto;
    }

    public static void main(String[] args) {
    }
}
