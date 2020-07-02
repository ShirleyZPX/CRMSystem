package com.springboot.crm.service;

import com.springboot.crm.entity.Area;
import com.springboot.crm.entity.Customer;
import com.springboot.crm.entity.Employee;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface CustomerService {

    String getCustomerNameById(Integer id);

    List<Customer> getCustomersInAllAreas(List<Area> areas);

    List<Customer> getCustomersByName(String customerName);


    List<Customer> getCustomersByAreaId(Integer areaId);

    boolean addCustomer(Customer customer);

    Customer checkCustomerAdd(String name, int operatorId);

    Map[] getCustomersByNameLikeByPage(String name, int id, int currentPage, int pageSize);

    Map[] getBusinessManagerByArea(int areaId);

    boolean editCustomer(int id, Customer customer);

    Map checkCustomerDelete(int id);

    void deleteCustomer(int id, int operatorId);

    Map[] getUnclaimedList(int operatorId, int currentPage, int pageSize);

    void claimCustomer(int id, int managerId, int operatorId);

    Map getCustomerFollowRecord(int id);

    Map[] findCustomer(String name, String previousName, String address, Integer areaId, String contactPerson, String phone1, String phone2, String customerManager, String managerPhone, Integer businessManagerId, Integer currentPage, Integer pageSize);

    Map getCustomerInfoWhenFind(int id);

    Map[] getAllUndeleteCustomers();

    Map[] getMainPage(int operatorId);

    Customer getCustomerById(int customerId);

    List<Integer> getCustomerIdsByCustomers(List<Customer> customers);

    List<Customer> getCustomersByNameAndAreaOfficial(int id, String customerName);

    Map getCustomerInfo(Customer customer);

    Customer getCustomerByName(String name);

    void downloadCustomerInfo(HttpServletResponse response, int id) throws IOException;

    void downloadAllCustomers(HttpServletResponse response) throws IOException;

    void downloadTemplate(HttpServletResponse response) throws UnsupportedEncodingException, FileNotFoundException;

    Map uploadCustomerInfo(Customer customer);

    void addFollowRecord(int id, String text, String date, String file, String type, int operatorId) throws ParseException;

    Map getCustomerInfoById(int id);

    void changeAreaNameByAreaId(Integer areaId, String name);

    List<Customer> getManagerByEmployee(Employee employee);

    void changeManagerOFCustomers(List<Customer> customers, int id, int handOverId, int operatorId);

    Map[] getAllCustomersByNameLikeByPage(String name, int id, int currentPage, int pageSize);
}
