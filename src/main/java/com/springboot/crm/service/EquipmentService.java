package com.springboot.crm.service;

import com.springboot.crm.entity.Area;
import com.springboot.crm.entity.Equipment;

import java.util.List;
import java.util.Map;

public interface EquipmentService {
    boolean addEquipment(Equipment equipment);

    Map getEquipmentsByNumber(String number);

    boolean editEquipment(int id, Equipment equipment);

    Equipment getEquipmentInfo(int id);

    Map checkDelete(String modelNumber);

    void deleteEquipment(int id, int operatorId);

    Map[] getAllEquipments();

    Equipment getEquipmentById(int equipId);

    List<Equipment> getEquipmentsByAreaId(int areaId);

    Map getAllEquipmentsInfo(int operatorId);

    Equipment getEquipmentByModelNum(String modelNumber);

    List<Integer> getEquipmentsByNum(String modelNumber);
}
