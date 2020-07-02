package com.springboot.crm.service;

import com.springboot.crm.entity.Orders;

public interface EditOrdersService {
    void addRecord(Orders order, int operatorId);
}
