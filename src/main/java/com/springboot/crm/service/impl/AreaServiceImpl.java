package com.springboot.crm.service.impl;

import com.springboot.crm.dao.AreaMapper;
import com.springboot.crm.dao.EmployeeAreaMapper;
import com.springboot.crm.entity.*;
import com.springboot.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    EmployeeAreaMapper employeeAreaMapper;
    @Autowired
    BusinessService businessService;
    @Autowired
    CustomerService customerService;
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    BackUpService backUpService;

    @Override
    public boolean insertArea(Area area) {
        try{
            areaMapper.insert(area);

            return true;
        } catch (DuplicateKeyException e) {
            //区域已存在
            return false;
        }
    }

    @Override
    public List<Area> getAllAreas() {
        return areaMapper.selectByExample(new AreaExample());
    }

    //修改区域信息
    @Override
    public boolean editArea(Integer areaId, Area area) {
        try{
            Area area1 = getAreaById(areaId);
            if (!area.getName().equals(area1.getName())) {
                customerService.changeAreaNameByAreaId(areaId, area.getName()); //change the area name when the name change
            }
            area.setId(areaId);
            areaMapper.updateByPrimaryKeySelective(area);
            return true;
        } catch (DuplicateKeyException e){
            //修改后的数据与已有数据重复
            return false;
        }
    }

    //删除区域
    @Override
    public String deleteArea(String areaName, String areaCode) {
        String info = ""; //record the related modules
        AreaExample areaExample = new AreaExample();
        AreaExample.Criteria areaCriteria = areaExample.createCriteria();
        areaCriteria.andNameEqualTo(areaName);
        areaCriteria.andAreaCodeEqualTo(areaCode);
        List<Area> list = areaMapper.selectByExample(areaExample);
        if (list.size() == 1) {
            Area area = list.get(0); //get the deleted area
            int areaId = area.getId();
            EmployeeAreaExample example = new EmployeeAreaExample();
            EmployeeAreaExample.Criteria criteria = example.createCriteria();
            criteria.andAreaIdEqualTo(areaId);
            long number = employeeAreaMapper.countByExample(example); //get the number of columns with area id=areaId
            Map[] businesses = businessService.getBusinessByAreaId(areaId);
            List<Customer> customers = customerService.getCustomersByAreaId(areaId);
            List<Equipment> equipments = equipmentService.getEquipmentsByAreaId(areaId);
            if (number > 0) { // the area is used in business module
                info += "User "; //employee module related
            }
            if (businesses.length > 0) {
                info += "Business ";
            }
            if (customers.size() > 0) {
                info += "Customer ";
            }
            if (equipments.size() > 0) {
                info += "Equipment ";
            }
            if(number == 0 && businesses.length == 0 && customers.size() == 0 && equipments.size() == 0) {
                areaMapper.deleteByPrimaryKey(areaId);
            }
        } else {
            info = "error"; //the deleted area didn't find
        }
        return info;
    }


    @Override
    public List<Area> getAreasByOperatorId(int operatorId) {
        EmployeeAreaExample example = new EmployeeAreaExample();
        EmployeeAreaExample.Criteria criteria = example.createCriteria();
        criteria.andEmployeeIdEqualTo(operatorId); //search the columns with operatorId
        List<EmployeeArea> list = employeeAreaMapper.selectByExample(example);
        List<Area> areas = new ArrayList<>();
        for (int i=0; i<list.size();i++) {
            int areaId = list.get(i).getAreaId();
            Area area = areaMapper.selectByPrimaryKey(areaId); //get the area by areaId
            areas.add(area);
        }
        return areas;
    }

    public List<Integer> getAreaIdsByAreas(List<Area> areas){
        List<Integer> areaIds = new ArrayList<>();
        for (int i=0; i<areas.size(); i++){
            int areaId = areas.get(i).getId();
            areaIds.add(areaId);
        }
        return areaIds;
    }

    @Override
    public Area getAreaByName(String name) {
        AreaExample example = new AreaExample();
        AreaExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<Area> areas = areaMapper.selectByExample(example);
        if (areas.size() == 0) {
            return null;
        } else {
            return areas.get(0);
        }

    }

    @Override
    public Area getAreaById(int areaId) {
        Area area = areaMapper.selectByPrimaryKey(areaId);
        return area;
    }


}
