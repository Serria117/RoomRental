/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

import com.controller.dto.BillDTO;
import com.controller.dto.RoomDTO;
import com.model.Bill;
import com.model.BillDetail;
import com.model.Contract;
import com.model.dao.BillDAO;
import com.model.dao.ContractDAO;
import com.model.dao.RoomDAO;
import com.model.dao.ServiceDAO;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author hadt2
 */
public class BillController {

    BillDAO billDAO = new BillDAO();
    ServiceDAO serDAO = new ServiceDAO();
    RoomDAO roomDAO = new RoomDAO();

    public boolean addBill(RoomDTO room, int quantity, int electricCount, int waterCount, String billNumber, int userId) {
        //Create a new bill and return billId;
        if (billDAO.isDuplicateBill(billNumber)) {
            return false;
        }
        ContractDAO cDAO = new ContractDAO();
        int roomId = room.getId();
        int lastElectricCount = Integer.parseInt(room.getElectricCounter());
        int lastWaterCount = Integer.parseInt(room.getWaterCounter());
        int roomPrice = Integer.parseInt(room.getPrice().replace(",", ""));
        Contract currentContract = cDAO.getCurrentContract(roomId); //get currently active contract of this room
        int contractId = currentContract.getId();

        Bill bill = new Bill();
        bill.setBillNumber(billNumber);
        bill.setContractId(contractId);
        bill.setRoomPrice(roomPrice);
        bill.setUserId(userId);
        bill.setRentalQuantity(quantity);

        int billId = billDAO.addBill(bill);
        if (billId < 1) {
            return false;
        }

        /*Now the bill has been created in the db, but it's still missing the total value of all service's price
        because they are not bound yet.*/
        //Get the service list (with price), for each service bind it with billId in the 'billDetail' table
        //insert services with fixed price per month:
        serDAO.GetAll().stream().filter(s -> s.getType() == 1).forEach(s -> {
            billDAO.updateBillDetail(billId, s.getId(), s.getPrice(), 1);
        });

        //Electric service:
        serDAO.GetAll().stream().filter(s -> s.getId() == 1).forEach(s -> {
            billDAO.updateBillDetail(billId, s.getId(), s.getPrice(), electricCount - lastElectricCount);
        });

        //Water service:
        serDAO.GetAll().stream().filter(s -> s.getId() == 2).forEach(s -> {
            billDAO.updateBillDetail(billId, s.getId(), s.getPrice(), waterCount - lastWaterCount);
        });

        //Update counters:
        roomDAO.updateCounters(roomId, electricCount, waterCount);

        return true;
    }

    public List<BillDTO> getBillByRoom(int RoomId) {
        return billDAO.getBillByRoom(RoomId).stream().map(b -> billModelToDTO(b)).collect(Collectors.toList());
    }

    public BillDTO getBillByNumber(String billNo) {
        return billModelToDTO(billDAO.getBillbyNumber(billNo));
    }

    public List<BillDetail> getBillDetails(String billNo) {
        return billDAO.getBillDetailByNumber(billNo);
    }

    public boolean updateBillStatus(String billNumber) {
        return billDAO.updateBillStatus(billNumber);
    }

    public static BillDTO billModelToDTO(Bill bill) {
        if (bill == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        BillDTO bdto = new BillDTO();
        bdto.setBillNumber(bill.getBillNumber());
        bdto.setContractId(bill.getContractId());
        bdto.setRoomPrice(bill.getRoomPrice());
        bdto.setStatus(bill.getStatus());
        bdto.setUserId(bill.getUserId());
        bdto.setTotal(bill.getTotal());
        bdto.setRentalQuantity(bill.getRentalQuantity());
        try {
            bdto.setCreatedDate(df.format(bill.getCreatedDate()));
            bdto.setUpdatedDate(df.format(bill.getUpdatedDate()));
        } catch (NullPointerException e) {
        }

        bdto.setId(bill.getId());

        return bdto;
    }

}
