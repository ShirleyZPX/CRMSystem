package com.springboot.crm.service;

import com.springboot.crm.entity.Type;

import java.util.Map;

public interface TypeService {
    void updateType(int id, Type type);

    int getIdByName(String name);

    Map[] getAllPermissions();

    Type getTypeById(int roleId);
}
