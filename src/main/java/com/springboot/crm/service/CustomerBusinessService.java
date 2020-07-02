package com.springboot.crm.service;

import com.springboot.crm.entity.Customer;
import com.springboot.crm.entity.CustomerBusiness;
import com.springboot.crm.entity.Employee;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface CustomerBusinessService {
    Map getBusinessListOfCustomersByPage(List<Customer> customers, int status, int currentPage, int pageSize);

    Map getBusinessListOfManagerByPage(Integer id, int status, Integer currentPage, Integer pageSize);

    Map getCustomerBusinessInfoById(Integer id);

    List<CustomerBusiness> getBusinessByCustomerId(int id);

    String[] getCustomerBusinessNameById(Integer businessCustomerId);

    CustomerBusiness getCustomerBusinessById(int customerBusinessId);

    Map getBusinessListOfTechByPage(Integer id, int status, Integer currentPage, Integer pageSize);

    List<CustomerBusiness> getBusinessesByBusinessId(int businessId);

    List<CustomerBusiness> getBusinessesByBusinessNumber(String businessNumber);

    List<CustomerBusiness> getBusinessesByBusinessManagerId(Integer businessManagerId);

    List<CustomerBusiness> getBusinessesByOrderNumber(String orderNumber);

    List<CustomerBusiness> getBusinessByCustomerIds(List<Integer> customerIds);

    List<CustomerBusiness> getBusinessesByOrderStatus(String orderStatus);

    Map getNumberOfOrdersStatusMonthly(List<Integer> businessIds);

    List<CustomerBusiness> getCustomerBusinessByBusinessId(Integer id);

    Map[] getCustomerBusinessInfoByCustomer(Customer customer);

    Map[] getMainPage();

    boolean addCustomerBusiness(CustomerBusiness customerBusiness);

    Map[] findBusinessByName(int id, String customerName, String businessName);

    boolean editCustomerBusiness(int id, CustomerBusiness customerBusiness);

    Map[] checkDelete(int id);

    void deleteCustomerBusiness(int id);

    Map[] getUnassignedBusiness(int currentPage, int pageSize);

    void assignBusiness(int id, int businessManagerId, int operatorId);

    Map[] findCustomerBusiness(String customerName, String businessName, String status, String installAddress, String orderNumber, String orderType, String orderAcceptTime, String orderReturnTime, String loadMode, String businessNumber, String bandWidth, String unicomCustomerId, String transactType, Integer packageNameId, String customerManager, String managerPhone, Integer businessManagerId, String orderStatus, Integer currentPage, Integer pageSize) throws ParseException;

    Map findInfoByBusinessNumber(String businessNumber);

    void exportCustomerBusinessInfos(HttpServletResponse response) throws IOException;

    void downloadTemplate(HttpServletResponse response) throws FileNotFoundException, UnsupportedEncodingException;

    List<CustomerBusiness> getManagerByEmployee(Employee employee);

    List<Integer> getIdsByCustomerBusinesses(List<CustomerBusiness> customerBusinesses);

    List<CustomerBusiness> getBusinessesNotFinished();

    void changeManagerOfBusiness(List<CustomerBusiness> customerBusinesses, int id, int handOverId, int operatorId);

    Map uploadCustomerBusinessInfo(CustomerBusiness customerBusiness);
    List<CustomerBusiness> getUninstallBusiness(int id);
}
