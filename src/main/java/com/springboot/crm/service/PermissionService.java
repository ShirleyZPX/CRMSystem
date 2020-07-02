package com.springboot.crm.service;

import com.springboot.crm.entity.OrderPermission;

import java.util.List;

public interface PermissionService {
    boolean addPermission(OrderPermission permission);

    List<Integer> getGoodIdsByOrderIds(List<Integer> orderIds);

    OrderPermission getPermissionById(Integer integer);
}
