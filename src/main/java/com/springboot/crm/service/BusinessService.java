package com.springboot.crm.service;

import com.springboot.crm.entity.Business;
import com.springboot.crm.entity.BusinessPackage;
import com.springboot.crm.entity.LoadMode;
import com.springboot.crm.entity.TransactType;

import java.util.List;
import java.util.Map;

public interface BusinessService {
    String getBusinessNameById(Integer businessId);


    Business getBusinessById(int businessId);


    Map[] getAllBusinessByName(String name, int currentPage, int pageSize);

    boolean editBusinessAndPackage(int id, Business businessRequest);

    String checkBusinessDelete(String name);

    void deleteBusiness(String name, int operatorId);

    boolean addBusinessAndPackage(Business businessRequest);

    Map[] getAllBusinessInfo();

    Map getBusinessByOperatorIdBypage(Integer operatorId, Integer currentPage, Integer pageSize);

    Map[] getBusinessByAreaId(int areaId);

    List<Business> getAllBusinesses();

    String getPackageNamebyId(int packageId);

    List<Business> getBusinessesByName(String businessName);

    List<Integer> getBusinessIdsByBuinesses(List<Business> businesses);

    String getPriceByPackageId(Integer packageNameId);

    Business getBusinessByName(String businessName);

    Integer getPackageIdByName(String packageName, int businessId);

    boolean addLoadMode(LoadMode loadMode);

    List<LoadMode> getLoadMode();

    boolean addTransactType(TransactType transactType);

    List<TransactType> getTransactType();

    List<BusinessPackage> getPackages(int businessId);
}
