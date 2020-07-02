package com.springboot.crm.service.impl;

import com.springboot.crm.dao.BusinessCustomerHistoryInfoMapper;
import com.springboot.crm.entity.BusinessCustomerHistoryInfo;
import com.springboot.crm.entity.CustomerBusiness;
import com.springboot.crm.service.CustomerBusinessHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomerBusinessHistoryServiceImpl implements CustomerBusinessHistoryService {
    @Autowired
    BusinessCustomerHistoryInfoMapper businessCustomerHistoryInfoMapper;

    @Override
    public void addRecord(CustomerBusiness customerBusiness, Integer operatorId, boolean delete) {
        BusinessCustomerHistoryInfo historyInfo = new BusinessCustomerHistoryInfo();
        historyInfo.setBusinessCustomerId(customerBusiness.getId());
        historyInfo.setBandWidth(customerBusiness.getBandWidth());
        historyInfo.setBusinessId(customerBusiness.getBusinessId());
        historyInfo.setBusinessManagerId(customerBusiness.getBusinessManagerId());
        historyInfo.setBusinessNumber(customerBusiness.getBusinessNumber());
        historyInfo.setCreateTime(customerBusiness.getCreateTime());
        historyInfo.setCustomerId(customerBusiness.getCustomerId());
        historyInfo.setCustomerManagerId(customerBusiness.getCustomerManagerId());
        if (delete == true) {
            historyInfo.setDataStatus("删除");
        } else {
            historyInfo.setDataStatus("修改");
        }
        historyInfo.setEditOperatorId(operatorId);
        historyInfo.setEditTime(new Date());
        historyInfo.setInstallAddress(customerBusiness.getInstallAddress());
        historyInfo.setLoadMode(customerBusiness.getLoadMode());
        historyInfo.setManagerPhone(customerBusiness.getManagerPhone());
        historyInfo.setOperatorId(customerBusiness.getOperatorId());
        historyInfo.setOrderAcceptTime(customerBusiness.getOrderAcceptTime());
        historyInfo.setOrderNumber(customerBusiness.getOrderNumber());
        historyInfo.setOrderReturnTime(customerBusiness.getOrderReturnTime());
        historyInfo.setOrderStatus(customerBusiness.getOrderStatus());
        historyInfo.setOrderType(customerBusiness.getOrderType());
        historyInfo.setPackageNameId(customerBusiness.getPackageNameId());
        historyInfo.setStatus(customerBusiness.getStatus());
        historyInfo.setTransactType(customerBusiness.getTransactType());
        historyInfo.setUnicomCustomerId(customerBusiness.getUnicomCustomerId());
        businessCustomerHistoryInfoMapper.insert(historyInfo); //add the record before update
    }
}
