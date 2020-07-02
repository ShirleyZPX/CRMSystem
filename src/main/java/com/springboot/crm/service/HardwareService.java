package com.springboot.crm.service;

import com.springboot.crm.entity.OrderHardware;

import java.util.List;
import java.util.Map;

public interface HardwareService {

    List<Integer> getGoodIdsByOrderIds(List<Integer> orderIds);

    OrderHardware getHardwareById(Integer integer);
}
