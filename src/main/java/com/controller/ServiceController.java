/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.controller.dto.ServiceDTO;
import com.model.Service;
import com.model.dao.ServiceDAO;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Nguyen Huu Tung
 */
public class ServiceController {

    static ServiceDAO serviceDAO = new ServiceDAO();

    public List<ServiceDTO> displayService() {
        return serviceDAO.GetAll().stream()
                .map(s -> ServiceModelToDTO(s))
                .collect(Collectors.toList());
    }

    public boolean addService(String serviceName, int price, String unit) {
        Service s = new Service(serviceName, price, unit);
        return serviceDAO.InsertService(s);
    }

    private static ServiceDTO ServiceModelToDTO(Service s) {

        NumberFormat numFormat = NumberFormat.getInstance();
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(s.getId());
        serviceDTO.setServiceName(s.getServiceName());
        serviceDTO.setPrice(numFormat.format(s.getPrice()));
        serviceDTO.setUnit(s.getUnit());
        serviceDTO.setType(s.getType());
        return serviceDTO;
    }
}
