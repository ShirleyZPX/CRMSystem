package com.springboot.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.crm.dao.EquipmentMapper;
import com.springboot.crm.entity.Area;
import com.springboot.crm.entity.Equipment;
import com.springboot.crm.entity.EquipmentExample;
import com.springboot.crm.service.AreaService;
import com.springboot.crm.service.BackUpService;
import com.springboot.crm.service.EquipmentService;
import com.springboot.crm.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    EquipmentMapper equipmentMapper;
    @Autowired
    GoodsService goodsService;
    @Autowired
    AreaService areaService;
    @Autowired
    BackUpService backUpService;


    @Override
    public boolean addEquipment(Equipment equipment) {
        try{
            equipmentMapper.insert(equipment);
            backUpService.addBackup(equipment.getOperatorId(), new Date(), "add equipment");
            return true;
        } catch (DuplicateKeyException e) {
            //已存在
            return false;
        }
    }

    @Override
    public Map getEquipmentsByNumber(String number) {
        EquipmentExample equipmentExample = new EquipmentExample();
        EquipmentExample.Criteria criteria = equipmentExample.createCriteria();
        number = "%" + number + "%";
        criteria.andModelNumberLike(number);
//        PageHelper.startPage(currentPage, pageSize);
        List<Equipment> equipments = equipmentMapper.selectByExample(equipmentExample);
//        PageInfo<Equipment> info = new PageInfo<>(equipments);
        Map[] maps = new Map[equipments.size()];
        for (int i=0; i<equipments.size(); i++){
            Equipment equipment = equipments.get(i);
            Map map = new HashMap();
            map.put("id", equipment.getId());
            map.put("modelNumber", equipment.getModelNumber());
            map.put("type", equipment.getType());
            map.put("area", areaService.getAreaById(equipment.getAreaId()).getName());
            map.put("manufacturer", equipment.getManufacturer());
            maps[i] = map;
        }
        Map data = new HashMap();
        data.put("data", maps);
        data.put("total", equipments.size());
        return data;
    }

    @Override
    public boolean editEquipment(int id, Equipment equipment) {
        try{
            equipment.setId(id);
            equipmentMapper.updateByPrimaryKeySelective(equipment);
            backUpService.addBackup(equipment.getOperatorId(), new Date(), "edit equipment");
            return true;
        } catch (DuplicateKeyException e){
            //修改后的数据与已有数据重复
            return false;
        }

    }

    @Override
    public Equipment getEquipmentInfo(int id) {
        Equipment equipment = equipmentMapper.selectByPrimaryKey(id);
        return equipment;
    }

    @Override
    public Map checkDelete(String modelNumber) {
        EquipmentExample example = new EquipmentExample();
        EquipmentExample.Criteria criteria = example.createCriteria();
        criteria.andModelNumberEqualTo(modelNumber);
        Equipment equipment = equipmentMapper.selectByExample(example).get(0);
        Map map = new HashMap();
        if (equipment == null){ //equipment doesn't exist
            map.put("info", "Not Found");
        } else {
            map.put("data", equipment);
        }
        boolean inStorage = goodsService.checkEquipmentInStorage(equipment);
        if (inStorage){
            map.put("info", "storage"); //has goods in storage
        } else {
            map.put("info", "delete");
        }
        return map;
    }

    @Override
    public void deleteEquipment(int id, int operatorId) {
        Equipment equipment = equipmentMapper.selectByPrimaryKey(id);
        equipment.setOperatorId(operatorId);
        equipmentMapper.deleteByPrimaryKey(id);
        backUpService.addBackup(operatorId, new Date(), "delete equipment");
    }

    @Override
    public Map[] getAllEquipments() {
        EquipmentExample equipmentExample = new EquipmentExample();
        EquipmentExample.Criteria criteria = equipmentExample.createCriteria();
        criteria.andIdIsNotNull();
        List<Equipment> equipments = equipmentMapper.selectByExample(equipmentExample);
        Map[] data = new Map[equipments.size()];
        for (int i=0; i<equipments.size(); i++){
            Equipment equipment = equipments.get(i);
            Map map = new HashMap();
            map.put("type", equipment.getType());
            map.put("modelNumber", equipment.getModelNumber());
            map.put("manufacturer", equipment.getManufacturer());
            map.put("area", areaService.getAreaById(equipment.getAreaId()).getName());
            data[i] = map;
        }
        return data;
    }

    @Override
    public Equipment getEquipmentById(int equipId) {
        return equipmentMapper.selectByPrimaryKey(equipId);
    }

    @Override
    public List<Equipment> getEquipmentsByAreaId(int areaId) {
        EquipmentExample equipmentExample = new EquipmentExample();
        EquipmentExample.Criteria criteria = equipmentExample.createCriteria();
        criteria.andAreaIdEqualTo(areaId);
        return equipmentMapper.selectByExample(equipmentExample);
    }

    @Override
    public Map getAllEquipmentsInfo(int operatorId) {
        List<Area> areas = areaService.getAreasByOperatorId(operatorId);
        List<Integer> areaIds = areaService.getAreaIdsByAreas(areas);
        List<Equipment> equipments = getEquipmentsByAreas(areaIds);
        List<Map> list = new ArrayList<>();
        for (int i=0; i<equipments.size(); i++) {
            Map map = new HashMap();
            Equipment equipment = equipments.get(i);
            map.put("equipmentId", equipment.getId());
            map.put("modelNumber", equipment.getModelNumber());
            int total = goodsService.getTotalGoodsNumByEquipment(equipment);
            int inStore = goodsService.getStoreGoodsNumByEquipment(equipment);
            map.put("total", String.valueOf(total));
            map.put("inStorage", String.valueOf(inStore));
            list.add(map);
        }
        Map data = new HashMap();
        data.put("list", list);
        data.put("total", equipments.size());
        return data;
    }

    @Override
    public Equipment getEquipmentByModelNum(String modelNumber) {
        EquipmentExample equipmentExample = new EquipmentExample();
        EquipmentExample.Criteria criteria = equipmentExample.createCriteria();
        criteria.andModelNumberEqualTo(modelNumber);
        List<Equipment> equipments = equipmentMapper.selectByExample(equipmentExample);
        if (equipments.size() == 0) {
            return null;
        } else {
            return equipments.get(0);
        }
    }

    @Override
    public List<Integer> getEquipmentsByNum(String modelNumber) {
        EquipmentExample equipmentExample = new EquipmentExample();
        EquipmentExample.Criteria criteria = equipmentExample.createCriteria();
        modelNumber = "%" + modelNumber + "%";
        criteria.andModelNumberLike(modelNumber);
        List<Equipment> equipmentList = equipmentMapper.selectByExample(equipmentExample);
        List<Integer> equipIds = new ArrayList<>();
        for (int i=0; i<equipmentList.size(); i++) {
            int id = equipmentList.get(i).getId();
            equipIds.add(id);
        }
        return equipIds;
    }

    private List<Equipment> getEquipmentsByAreas(List<Integer> areaIds) {
        EquipmentExample equipmentExample = new EquipmentExample();
        EquipmentExample.Criteria criteria = equipmentExample.createCriteria();
        criteria.andAreaIdIn(areaIds);
        return equipmentMapper.selectByExample(equipmentExample);
    }
}
