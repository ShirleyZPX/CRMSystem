package com.springboot.crm.service.impl;

import com.springboot.crm.dao.OrderPermissionMapper;
import com.springboot.crm.entity.OrderHardware;
import com.springboot.crm.entity.OrderHardwareExample;
import com.springboot.crm.entity.OrderPermission;
import com.springboot.crm.entity.OrderPermissionExample;
import com.springboot.crm.service.PermissionService;
import com.springboot.crm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    ProductService productService;
    @Autowired
    OrderPermissionMapper permissionMapper;

    @Override
    public boolean addPermission(OrderPermission permission) {
        if(productService.addProduct(permission)) {
            permissionMapper.insert(permission);
            return true;
        }
        return false;
    }

    @Override
    public List<Integer> getGoodIdsByOrderIds(List<Integer> orderIds) {
        OrderPermissionExample example = new OrderPermissionExample();
        OrderPermissionExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdIn(orderIds);
        List<OrderPermission> permissions = permissionMapper.selectByExample(example);
        List<Integer> goodIds = new ArrayList<>();
        for (int i=0; i<permissions.size(); i++){
            OrderPermission permission = permissions.get(i);
            int goodId = permission.getGoodsId();
            goodIds.add(goodId);
        }
        return goodIds;
    }

    @Override
    public OrderPermission getPermissionById(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }
}
