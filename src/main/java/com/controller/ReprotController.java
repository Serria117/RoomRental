/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

import com.controller.dto.ReportDTO;
import com.model.Report;
import com.model.dao.ReprotDAO;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Quan Le
 */
public class ReprotController {

    public ReprotDAO rp = new ReprotDAO();

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

    public Double totalPay() {
        return rp.payment().stream().mapToDouble(x -> x.getTotal()).sum();

    }

    public Double totalNoPay() {
        return rp.notPayment().stream().mapToDouble(x -> x.getTotal()).sum();
    }

    public static ReportDTO ReportModelToDTO(Report r) {

        NumberFormat numFormat = NumberFormat.getInstance();

        ReportDTO repto = new ReportDTO();
        repto.setRoomNumber(r.getRoomNumber());
        repto.setTotal(r.getTotal());
        // đổi ngày tháng
        // repto.setTime(numFormat.format(r.getTime()))
        return repto;

    }

    public static void main(String[] args) {
        ReprotController rpController = new ReprotController();
        System.out.println(rpController.totalNoPay());
    }
}
