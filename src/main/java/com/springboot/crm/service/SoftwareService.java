package com.springboot.crm.service;

import com.springboot.crm.entity.OrderSoftware;

import java.util.List;

public interface SoftwareService {
    boolean addSoftware(OrderSoftware software);

    List<Integer> getGoodIdsByOrderIds(List<Integer> orderIds);

    OrderSoftware getSoftwareById(Integer integer);
}
