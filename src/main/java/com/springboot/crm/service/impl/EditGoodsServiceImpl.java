package com.springboot.crm.service.impl;

import com.springboot.crm.dao.EditGoodsMapper;
import com.springboot.crm.entity.EditGoods;
import com.springboot.crm.entity.Goods;
import com.springboot.crm.service.EditGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EditGoodsServiceImpl implements EditGoodsService {
    @Autowired
    EditGoodsMapper editGoodsMapper;

    @Override
    public void addRecord(Goods goods, int operatorId) {
        EditGoods editGoods = new EditGoods();
        editGoods.setCreateTime(goods.getCreateTime());
        editGoods.setDeviceCode(goods.getDeviceCode());
        editGoods.setEditOperaterId(operatorId);
        editGoods.setEditTime(new Date());
        editGoods.setEquipmentId(goods.getEquipmentId());
        editGoods.setGoodsId(goods.getId());
        editGoods.setHardwareCode(goods.getHardwareCode());
        editGoods.setInStoragePeopleId(goods.getInStoragePeopleId());
        editGoods.setInStorageTime(goods.getInStorageTime());
        editGoods.setNumber(goods.getNumber());
        editGoods.setOperatorId(goods.getOperatorId());
        if (goods.getOutStoragePeopleId() != null) {
            editGoods.setOutStoragePeopleId(goods.getOutStoragePeopleId());
            editGoods.setOutStorageTime(goods.getOutStorageTime());
        }
        editGoods.setPurchaseTime(goods.getPurchaseTime());
        editGoods.setStatus(goods.getStatus());
        editGoods.setStorageAreaId(goods.getStorageAreaId());
        if (goods.getUsingCustomerId() != null) {
            editGoods.setUsingCustomerId(goods.getUsingCustomerId());
        }
        editGoodsMapper.insert(editGoods);
    }
}
