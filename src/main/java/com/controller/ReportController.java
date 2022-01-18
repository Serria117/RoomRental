/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

import com.controller.dto.ReportDTO;
import com.model.Report;
import com.model.dao.ReportDAO;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Quan Le
 */
public class ReportController {

    public ReportDAO rp = new ReportDAO();

    public List<ReportDTO> showPay() {
        // trả về 1 chuổi theo dạng DTO để show ra màn hình
        return rp.payment().stream()
                .map(r -> ReportModelToDTO(r))
                .collect(Collectors.toList());
    }

    public List<ReportDTO> showNotPay() {
        // trả về 1 chuổi theo dạng DTO để show ra màn hình
        return rp.notPayment().stream()
                .map(r -> ReportModelToDTO(r))
                .collect(Collectors.toList());
    }

    public String totalPay() {
        Double Pay = rp.payment().stream().mapToDouble(x -> x.getTotal()).sum();
        String totalPay = NumberFormat.getInstance().format(Pay);
        return totalPay;
    }

    public String totalNoPay() {
        Double noPay = rp.notPayment().stream().mapToDouble(x -> x.getTotal()).sum();
        String totalNoPay = NumberFormat.getInstance().format(noPay);
        return totalNoPay;
    }

    public static ReportDTO ReportModelToDTO(Report r) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ReportDTO repto = new ReportDTO();
        repto.setRoomNumber(r.getRoomNumber());
        repto.setTotal(r.getTotal());
        try {
            repto.setCreatedDate(df.format(r.getCreatedDate()));
        } catch (Exception e) {
        }
        try {
            repto.setUpdatedDate(df.format(r.getUpdatedDate()));
        } catch (Exception e) {
        }
        return repto;

    }

    public static void main(String[] args) {
        ReportController rpController = new ReportController();
        System.out.println(rpController.totalNoPay());
    }
}
