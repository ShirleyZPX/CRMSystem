package com.springboot.crm.service;

import com.springboot.crm.entity.*;
import com.springboot.crm.request.Schedule;
import com.springboot.crm.request.UserList;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface OrderService {
    Map getOrderList(Integer id, Integer currentPage, Integer pageSize, int status) throws ParseException;

    Map getOrderInfoById(Integer id, Integer status);

    List<UserList> getAllTechnicists(int id);

    List<Map> getScheduleOfUser(int id);

    void sendOrder(int customerBusinessId, int operatorId, int technicistId, String installTime) throws ParseException;

    List<Orders> getOrdersHoldByTech(int id);

    void collectOrder(int id, int operatorId);

    void reassignedOrder(int id, int operatorId, int techId, String installTime) throws ParseException;

    boolean returnOrder(int id, Map mapper);


    Orders getOrderById(int orderId);

    void withdrawOrder(int id, String withdrawReason, int operatorId);

    Map findOrderByPage(String customerName, String businessId, String businessNumber, Integer businessManagerId, String orderNumber, Integer technicistId, Integer areaId, String orderStatus, String orderStartTime, String orderEndTime, Integer currentPage, Integer pageSize, Integer operatorId) throws ParseException;

    Map getNumberOfEachStatusInCondition(String customerName, String businessId, String businessNumber, Integer businessManagerId, String orderNumber, Integer technicistId, Integer areaId, String orderStatus, String orderStartTime, String orderEndTime, Integer operatorId) throws ParseException;

    Map[] getOrdersInfoAndSchedule(int operatorId);

    Map[] getInstalledDeviceByCustomerBusinessIds(List<Integer> customerBusinessIds);

    List<Orders> getOrdersUncompleteByTech(Employee employee);

    void changeAcceptPeopleOfOrders(List<Orders> orders, int id, int handOverId, int operatorId);
}
