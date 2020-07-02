package com.springboot.crm.service;

import com.springboot.crm.entity.Equipment;
import com.springboot.crm.entity.Goods;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface GoodsService {
    Goods getGoodsByGoodsCode(String goodsCode);

    boolean checkEquipmentInStorage(Equipment equipment);

    Goods getGoodById(Integer integer);

    String getModelNumber(Goods good);

    int getTotalGoodsNumByEquipment(Equipment equipment);

    int getStoreGoodsNumByEquipment(Equipment equipment);

    String inStoreGoods(String id, String buyDate, String inStoragePeopleId, String inStorageTime, String hardwareCode, String areaId, List<Map> deviceCodes, String operatorId) throws ParseException;

    Map checkRecycleGoodsByDeviceCode(String deviceCode, String deviceNumber);

    void recycleGoods(int id, int operatorId);

    Map checkOutStoreGoods(String deviceCode, String deviceNumber, Integer collectedPeopleId);

    void outStoreGoods(int id, int operatorId);

    List<Map> getTransferGoods(int id, int areaId1, int areaId2);

    void transferGoods(List<Integer> ids, int areaId1, int areaId2, String operatorId);

    List<Map> getCheckGoodsInfo(int id, int areaId);

    void recordGoods(int id, String status);

    List<Map> getGoodsRecords(Boolean unusual, Integer areaId);

    Map getGoodsInfoByDeviceNum(String deviceNumber);

    void editGoods(int id, String modelNumber, String status, String deviceNumber, String buyDate, String inStorageTime, int inStoragePeopleId, String outStorageTime, int outStoragePeopleId, String hardwareCode, int areaId, String deviceCode, String operatorId) throws ParseException;

    Map checkChangesList(Integer currentPage, Integer pageSize);

    Map getSpecificGoodChanges(int id);

    void changeGoodSuggest(int id, Boolean result, int operatorId);

    Map findGoods(String modelNumber, String status, String deviceNumber, String buyDate, String customerName, String inStorageTime, Integer inStoragePeopleId, String outStorageTime, Integer outStoragePeopleId, String deviceCode, String hardwareCode, Integer areaId, Integer currentPage, Integer pageSize) throws ParseException;

}
