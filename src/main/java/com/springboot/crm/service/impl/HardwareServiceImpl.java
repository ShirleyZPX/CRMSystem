package com.springboot.crm.service.impl;

import com.springboot.crm.dao.OrderHardwareMapper;
import com.springboot.crm.entity.OrderHardware;
import com.springboot.crm.entity.OrderHardwareExample;
import com.springboot.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HardwareServiceImpl implements HardwareService {
    @Autowired
    OrderHardwareMapper hardwareMapper;
    @Autowired
    ProductService productService;


    @Override
    public List<Integer> getGoodIdsByOrderIds(List<Integer> orderIds) {
        OrderHardwareExample example = new OrderHardwareExample();
        OrderHardwareExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdIn(orderIds);
        List<OrderHardware> hardwares = hardwareMapper.selectByExample(example);
        List<Integer> goodIds = new ArrayList<>();
        for (int i=0; i<hardwares.size(); i++){
            OrderHardware hardware = hardwares.get(i);
            int goodId = hardware.getGoodsId();
            goodIds.add(goodId);
        }
        return goodIds;
    }

    @Override
    public OrderHardware getHardwareById(Integer id) {
        return hardwareMapper.selectByPrimaryKey(id);
    }
}
