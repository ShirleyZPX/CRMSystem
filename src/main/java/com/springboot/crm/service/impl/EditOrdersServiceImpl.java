package com.springboot.crm.service.impl;

import com.springboot.crm.dao.EditOrdersMapper;
import com.springboot.crm.entity.EditOrders;
import com.springboot.crm.entity.Orders;
import com.springboot.crm.service.EditOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EditOrdersServiceImpl implements EditOrdersService {
    @Autowired
    EditOrdersMapper editOrdersMapper;


    @Override
    public void addRecord(Orders order, int operatorId) {
        EditOrders editOrders = new EditOrders();
        editOrders.setOrderId(order.getId());
        if (order.getAcceptOrderPeopleId() != null) {
            editOrders.setAcceptOrderPeopleId(order.getAcceptOrderPeopleId());
        }
        editOrders.setBusinessCustomerId(order.getBusinessCustomerId());
        editOrders.setEditTime(new Date());
        if (order.getInstallTime() != null) {
            editOrders.setInstallTime(order.getInstallTime());
        }
        if (order.getReturnedReason() != null) {
            editOrders.setReturnedReason(order.getReturnedReason());
        }
        editOrders.setOperatorId(operatorId);
        editOrdersMapper.insert(editOrders);


    }
}
