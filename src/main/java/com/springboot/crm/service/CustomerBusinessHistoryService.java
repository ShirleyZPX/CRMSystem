package com.springboot.crm.service;

import com.springboot.crm.entity.CustomerBusiness;

public interface CustomerBusinessHistoryService {

    void addRecord(CustomerBusiness customerBusiness, Integer operatorId, boolean delete);
}
