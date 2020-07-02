package com.springboot.crm.service;

import com.springboot.crm.entity.Goods;

public interface EditGoodsService {
    void addRecord(Goods goods, int operatorId);
}
