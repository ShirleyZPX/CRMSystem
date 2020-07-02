package com.springboot.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.crm.dao.BusinessMapper;
import com.springboot.crm.dao.BusinessPackageMapper;
import com.springboot.crm.dao.LoadModeMapper;
import com.springboot.crm.dao.TransactTypeMapper;
import com.springboot.crm.entity.*;
import com.springboot.crm.service.AreaService;
import com.springboot.crm.service.BackUpService;
import com.springboot.crm.service.BusinessService;
import com.springboot.crm.service.CustomerBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BusinessServiceImpl implements BusinessService {
    @Autowired
    BusinessMapper businessMapper;
    @Autowired
    BusinessPackageMapper packageMapper;
    @Autowired
    CustomerBusinessService customerBusinessService;
    @Autowired
    AreaService areaService;
    @Autowired
    BackUpService backUpService;
    @Autowired
    LoadModeMapper loadModeMapper;
    @Autowired
    TransactTypeMapper transactTypeMapper;

    private final static String CHARGE_YEAR = "包年";
    private final static String CHARGE_MONTH = "包月";

    @Override
    public String getBusinessNameById(Integer businessId) {
        String name = getBusinessById(businessId).getName();
        return name;
    }

    @Override
    public Business getBusinessById(int businessId) {
        Business business = businessMapper.selectByPrimaryKey(businessId);
        return business;
    }

    @Override
    public Map[] getAllBusinessByName(String name, int currentPage, int pageSize) {
        BusinessExample example = new BusinessExample();
        BusinessExample.Criteria criteria = example.createCriteria();
        if (name.equals("null")) {
            criteria.andNameIsNotNull();
        } else {
            name = "%" + name + "%";
            criteria.andNameLike(name); //名称模糊查询
        }
        PageHelper.startPage(currentPage, pageSize);
        List<Business> businesses = businessMapper.selectByExample(example);
        PageInfo<Business> info = new PageInfo<>(businesses);
        List<Business> businesses1 = info.getList();
        return getBusinessInfo(businesses1);
    }


    private boolean editBusiness(int id, Business business) {
        try{
            business.setId(id);
            if (business.getChargeMode() != null) {//for year
                if (business.getChargeMode().equals("true")) {
                    business.setchargeType(CHARGE_YEAR);
                } else {
                    business.setchargeType(CHARGE_MONTH);
                }
            }
            businessMapper.updateByPrimaryKeySelective(business);
            backUpService.addBackup(business.getOperatorId(), new Date(), "edit business");
            return true;
        } catch (DuplicateKeyException e){
            //修改后的数据与已有数据重复
            return false;
        }
    }

    public boolean editBusinessAndPackage(int id, Business business){
        if (editBusiness(id, business)){
            List<BusinessPackage> businessPackages = getPackagesByBusinessId(id); //get all the packages
            List<Integer> packageIds = getPackageIdsByPackages(businessPackages);
            BusinessPackageExample example = new BusinessPackageExample();
            BusinessPackageExample.Criteria criteria = example.createCriteria();
            criteria.andIdIn(packageIds);
            packageMapper.deleteByExample(example); //delete the packages row of the business
            addPackages(id, business); //add packages of this business
            return true;
        } else {
            return false;
        }
    }

    private List<Integer> getPackageIdsByPackages(List<BusinessPackage> packages){
        List<Integer> packageIds = new ArrayList<>();
        for (int i=0; i<packages.size(); i++){
            int packageId = packages.get(i).getId();
            packageIds.add(packageId);
        }
        return packageIds;
    }

    public Business getBusinessByName(String name) {
        BusinessExample example = new BusinessExample();
        BusinessExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<Business> businesses = businessMapper.selectByExample(example);
        if (businesses.size() == 0) {
            return null;
        } else {
            return businesses.get(0);
        }
    }

    @Override
    public Integer getPackageIdByName(String packageName, int businessId) {
        BusinessPackageExample example =new BusinessPackageExample();
        BusinessPackageExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessIdEqualTo(businessId);
        criteria.andPackageNameEqualTo(packageName);
        return packageMapper.selectByExample(example).get(0).getId();
    }

    @Override
    public boolean addLoadMode(LoadMode loadMode) {
        try{
            loadModeMapper.insert(loadMode);
            return true;
        } catch (DuplicateKeyException e) {
            //业务已存在
            return false;
        }
    }

    @Override
    public List<LoadMode> getLoadMode() {
        LoadModeExample example = new LoadModeExample();
        LoadModeExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        List<LoadMode> loadModes = loadModeMapper.selectByExample(example);
        return loadModes;
    }

    @Override
    public boolean addTransactType(TransactType transactType) {
        try{
            transactTypeMapper.insert(transactType);
            return true;
        } catch (DuplicateKeyException e) {
            //业务已存在
            return false;
        }
    }

    @Override
    public List<TransactType> getTransactType() {
        TransactTypeExample example = new TransactTypeExample();
        TransactTypeExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        List<TransactType> transactTypes = transactTypeMapper.selectByExample(example);
        return transactTypes;
    }

    @Override
    public List<BusinessPackage> getPackages(int businessId) {
        BusinessPackageExample example = new BusinessPackageExample();
        BusinessPackageExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessIdEqualTo(businessId);
        List<BusinessPackage> list = packageMapper.selectByExample(example);

        return list;
    }

    @Override
    public String checkBusinessDelete(String name) {
        String info="";
        Business business = getBusinessByName(name);
        if (business == null){
            info = "error"; //business doesn't exist
        } else {
            //check if the business used in customer business
            List<CustomerBusiness> customerBusinesses = customerBusinessService.getCustomerBusinessByBusinessId(business.getId());
            if (customerBusinesses.size() == 0){ //no business used in customer business
                info = "success";
            } else {
                info += "Customer ";
            }
        }
        return info;
    }


    @Override
    public void deleteBusiness(String name, int operatorId) {
        Business business = getBusinessByName(name);
        deletePackagesByBusiness(business);
        businessMapper.deleteByPrimaryKey(business.getId());
        backUpService.addBackup(operatorId, new Date(), "delete business");
    }

    private void deletePackagesByBusiness(Business business) {
        BusinessPackageExample example = new BusinessPackageExample();
        BusinessPackageExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessIdEqualTo(business.getId());
        packageMapper.deleteByExample(example);
    }


    private boolean addBusiness(Business business) {
        try{
            if (business.getChargeMode()!= null) {
                if (business.getChargeMode().equals("true")) {
                    business.setchargeType(CHARGE_YEAR);
                } else {
                    business.setchargeType(CHARGE_MONTH);
                }
            }
            business.setEditTime(new Date());
             businessMapper.insert(business);
             backUpService.addBackup(business.getOperatorId(), new Date(), "add business");
             return true;
        } catch (DuplicateKeyException e) {
            //业务已存在
            return false;
        }
    }

    private List<BusinessPackage> getPackagesByBusinessId(int businessId){
        BusinessPackageExample example = new BusinessPackageExample();
        BusinessPackageExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessIdEqualTo(businessId);
        return packageMapper.selectByExample(example);
    }

    private void addPackages(int businessId, Business business) {
        Map[] packages = business.getPackages();
        for (int i=0; i<packages.length; i++){
            Map packageMap = packages[i];
            String packageName = (String) packageMap.get("packageName");
            String packagePrice = (String) packageMap.get("packagePrice");
            BusinessPackage businessPackage = new BusinessPackage();
            businessPackage.setBusinessId(businessId);
            businessPackage.setPackageName(packageName);
            businessPackage.setPackagePrice(packagePrice);
            packageMapper.insert(businessPackage);
        }
    }

    public boolean addBusinessAndPackage(Business business){
        if(addBusiness(business)){
            Business business1 = getBusinessByName(business.getName());
            int businessId = business1.getId();
            addPackages(businessId, business);
            return true;
        } else {
            return false;
        }
    }

    private Map[] getBusinessInfo(List<Business> businesses){
        Map[] data = new Map[businesses.size()];
        for (int i=0; i<businesses.size(); i++){
            Business business = businesses.get(i);
            int businessId = business.getId();
            List<BusinessPackage> businessPackages = getPackagesByBusinessId(businessId); //get all the packages of the business
            Map[] packages = new Map[businessPackages.size()];
            for (int j=0; j<businessPackages.size(); j++){
                BusinessPackage businessPackage = businessPackages.get(j);
                Map packageMap = new HashMap();
                packageMap.put("packageName", businessPackage.getPackageName());
                packageMap.put("packagePrice", businessPackage.getPackagePrice());
                packages[j] = packageMap;
            }
            int areaId = business.getAreaId();
            String areaName = areaService.getAreaById(areaId).getName();
            Map businessMap = new HashMap();
            businessMap.put("id", businessId);
            businessMap.put("name", business.getName());
            businessMap.put("province", business.getProvince());
            businessMap.put("area", areaName);
            businessMap.put("priceType", business.getchargeType());
            businessMap.put("packages", packages);
            data[i] = businessMap;
        }
        return data;
    }

    @Override
    public Map[] getAllBusinessInfo() { //get all the businesses
        BusinessExample example = new BusinessExample();
        BusinessExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        List<Business> businesses = businessMapper.selectByExample(example);
        return getBusinessInfo(businesses);
    }


    @Override
    public Map getBusinessByOperatorIdBypage(Integer operatorId, Integer currentPage, Integer pageSize) {
        Map map = new HashMap();
        List<Area> areas = areaService.getAreasByOperatorId(operatorId);
        List<Integer> areaIds = areaService.getAreaIdsByAreas(areas);
        BusinessExample example = new BusinessExample();
        BusinessExample.Criteria criteria = example.createCriteria();
        criteria.andAreaIdIn(areaIds);
        PageHelper.startPage(currentPage, pageSize);
        List<Business> businesses = businessMapper.selectByExample(example);
        PageInfo<Business> info = new PageInfo<>(businesses);
        map.put("total", info.getTotal());
        List<Business> pageBusiness = info.getList();
        Map[] list = new Map[pageBusiness.size()];
        for (int i=0; i<pageBusiness.size(); i++){
            Map businessMap = new HashMap();
            Business business = pageBusiness.get(i);
            businessMap.put("businessesId", business.getId());
            businessMap.put("businessesName", business.getName());
            businessMap.put("areaId", business.getAreaId());
            businessMap.put("areaName", areaService.getAreaById(business.getAreaId()).getName());
            list[i] = businessMap;
        }
        map.put("list", list);
        return map;
    }

    @Override
    public Map[] getBusinessByAreaId(int areaId) {
        BusinessExample example = new BusinessExample();
        BusinessExample.Criteria criteria = example.createCriteria();
        criteria.andAreaIdEqualTo(areaId);
        List<Business> businesses = businessMapper.selectByExample(example);
        Map[] data = new Map[businesses.size()];
        for (int i=0; i<businesses.size(); i++){
            Map businessMap = new HashMap();
            Business business = businesses.get(i);
            businessMap.put("id", business.getId());
            businessMap.put("name", business.getName());
            data[i] = businessMap;
        }
        return data;
    }

    @Override
    public List<Business> getAllBusinesses() {
        BusinessExample example = new BusinessExample();
        BusinessExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        return businessMapper.selectByExample(example);
    }

    @Override
    public String getPackageNamebyId(int packageId) {
        BusinessPackage businessPackage = packageMapper.selectByPrimaryKey(packageId);
        return businessPackage.getPackageName();
    }

    @Override
    public List<Business> getBusinessesByName(String businessName) {
        BusinessExample example = new BusinessExample();
        BusinessExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(businessName);
        return businessMapper.selectByExample(example);
    }

    @Override
    public List<Integer> getBusinessIdsByBuinesses(List<Business> businesses) {
        List<Integer> businessIds = new ArrayList<>();
        for (int i=0; i<businesses.size(); i++){
            Business business = businesses.get(i);
            businessIds.add(business.getId());
        }
        return businessIds;
    }

    @Override
    public String getPriceByPackageId(Integer packageNameId) {
        BusinessPackage businessPackage = packageMapper.selectByPrimaryKey(packageNameId);
        return businessPackage.getPackagePrice();
    }


}
