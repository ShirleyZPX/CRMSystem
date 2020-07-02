package com.springboot.crm.request;

import java.util.Date;

public class Order {
    private int id; //（未派单）客户业务的id / （已派单）工单id
    private int customerId;
    private String customerName;
    private int businessId;
    private String businessName;
    private String businessNumber;
    private String bandWidth;
    private String businessManager;
    private Date orderAcceptTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getBandWidth() {
        return bandWidth;
    }

    public void setBandWidth(String bandWidth) {
        this.bandWidth = bandWidth;
    }

    public String getBusinessManager() {
        return businessManager;
    }

    public void setBusinessManager(String businessManager) {
        this.businessManager = businessManager;
    }

    public Date getOrderAcceptTime() {
        return orderAcceptTime;
    }

    public void setOrderAcceptTime(Date orderAcceptTime) {
        this.orderAcceptTime = orderAcceptTime;
    }

}
