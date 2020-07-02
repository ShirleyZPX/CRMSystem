package com.springboot.crm.service.impl;

import com.springboot.crm.dao.TypeMapper;
import com.springboot.crm.entity.Type;
import com.springboot.crm.entity.TypeExample;
import com.springboot.crm.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeMapper typeMapper;

    @Override
    public void updateType(int typeId, Type type) {

        type.setId(typeId);
        typeMapper.updateByPrimaryKeySelective(type);
    }

    @Override
    public int getIdByName(String name) {
        TypeExample example = new TypeExample();
        TypeExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        int id = typeMapper.selectByExample(example).get(0).getId();
        return id;
    }

    @Override
    public Map[] getAllPermissions() {
        TypeExample example = new TypeExample();
        TypeExample.Criteria criteria = example.createCriteria();
        criteria.andNameIsNotNull();
        List<Type> types = typeMapper.selectByExample(example);
        Map[] data = new Map[types.size()];
        for (int i=0; i<types.size(); i++){
            Type type = types.get(i);
            Map map = new HashMap();
            map.put("id", type.getId());
            map.put("name", type.getName());
            data[i] = map;
        }
        return data;
    }

    @Override
    public Type getTypeById(int roleId) {
        Type type = typeMapper.selectByPrimaryKey(roleId);
        return type;
    }
}
