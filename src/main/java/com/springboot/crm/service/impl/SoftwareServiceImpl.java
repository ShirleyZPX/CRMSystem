package com.springboot.crm.service.impl;

import com.springboot.crm.dao.OrderSoftwareMapper;
import com.springboot.crm.entity.OrderHardware;
import com.springboot.crm.entity.OrderHardwareExample;
import com.springboot.crm.entity.OrderSoftware;
import com.springboot.crm.entity.OrderSoftwareExample;
import com.springboot.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SoftwareServiceImpl implements SoftwareService {
    @Autowired
    OrderSoftwareMapper softwareMapper;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    CustomerBusinessService customerBusinessService;
    @Autowired
    ProductService productService;

    @Override
    public boolean addSoftware(OrderSoftware software) {
        if(productService.addProduct(software)) {
            softwareMapper.insert(software);
            return true;
        }
        return false;
    }

    @Override
    public List<Integer> getGoodIdsByOrderIds(List<Integer> orderIds) {
        OrderSoftwareExample example = new OrderSoftwareExample();
        OrderSoftwareExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdIn(orderIds);
        List<OrderSoftware> softwares = softwareMapper.selectByExample(example);
        List<Integer> goodIds = new ArrayList<>();
        for (int i=0; i<softwares.size(); i++){
            OrderSoftware software = softwares.get(i);
            int goodId = software.getGoodsId();
            goodIds.add(goodId);
        }
        return goodIds;
    }

    @Override
    public OrderSoftware getSoftwareById(Integer id) {
        return softwareMapper.selectByPrimaryKey(id);
    }
}
