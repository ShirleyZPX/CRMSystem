package com.springboot.crm.controller;

import com.springboot.crm.entity.OrderHardware;
import com.springboot.crm.entity.OrderPermission;
import com.springboot.crm.entity.OrderSoftware;
import com.springboot.crm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RequestMapping("/crm/")
@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("order/list")
    public Map getOrdersList(@RequestParam(value = "id") Integer id, @RequestParam(value = "status") Integer status, @RequestParam(value = "currentPage") Integer currentPage, @RequestParam(value = "pageSize") Integer pageSize) throws ParseException {
        Map<String , Object> map = new HashMap<>();
        map.put("status", "OK");
        if(status == 0) { //未派单
            map.put("message", "Return all unassigned orders");
        } else if(status == 1) { //已派单
            map.put("message", "Return all assigned orders");
        } else { //已收单
            map.put("message", "Return all returned orders");
        }
        Map<String, Object> dataMap = orderService.getOrderList(id, currentPage, pageSize, status);
        map.put("data", dataMap);
        return map;
    }

    @GetMapping("order/detail")
    public Map getOrderInfoById(@RequestParam(value = "id") Integer id, @RequestParam(value = "status") Integer status) {
        Map<String, Object> map = new HashMap<>();
        Map data = orderService.getOrderInfoById(id, status);
        map.put("status", "OK");
        map.put("message", "Return order information");
        map.put("data",data);
        return map;
    }

    @GetMapping("order/technicists/{id}")
    public Map getAllTechnicists(@PathVariable int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        map.put("message", "Return all technicists");
        map.put("data",orderService.getAllTechnicists(id));
        return map;
    }

    @GetMapping("order/technicist/assigned/{id}")
    public Map getScheduleOfUser(@PathVariable int id){
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        map.put("message", "Return information about technicist");
        map.put("data", orderService.getScheduleOfUser(id));
        return map;
    }

    @PostMapping("order/send")
    public Map sendOrder(@RequestBody Map<String, Object> mapper) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        int customerBusinessId = (int) mapper.get("id");
        int technicistId = (int) mapper.get("technicistId");
        String installTime = (String) mapper.get("installTime");
        int operatorId = (int) mapper.get("operatorId");
        orderService.sendOrder(customerBusinessId, operatorId, technicistId, installTime);
        map.put("status", "OK");
        map.put("message", "Assigned order successful");
        return map;
    }

    @PutMapping("order/receive/{id}/{operatorId}")
    public Map collectOrder(@PathVariable int id, @PathVariable int operatorId){
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        map.put("message", "Receive order successful");
        orderService.collectOrder(id, operatorId);
        return map;
    }

    @PutMapping("order/reassigned/{id}")
    public Map reassignedOrder(@PathVariable int id, @RequestBody Map<String, Object> mapper) throws ParseException {
        int techId = (int) mapper.get("technicistId");
        String installTime = (String) mapper.get("installTime");
        int operatorId = (int) mapper.get("operatorId");
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        map.put("message", "Reassigned order successful");
        orderService.reassignedOrder(id, operatorId, techId, installTime);
        return map;
    }

    @PutMapping("order/return/{id}")
    public Map returnOrder(@PathVariable int id, @RequestBody Map mapper){
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        map.put("message", "Return order successful");
        if(orderService.returnOrder(id, mapper)) {

        }
        return map;
    }


    @PutMapping("order/withdraw/{id}")
    public Map withdrawOrder(@PathVariable int id, @RequestBody Map<String, Object> mapper) {
        String withdrawReason = (String) mapper.get("withdrawReason");
        int operatorId = (int) mapper.get("operatorId");
        Map<String, Object> map = new HashMap<>();
        orderService.withdrawOrder(id, withdrawReason, operatorId);
        map.put("status", "OK");
        map.put("message", "Withdraw order successful");
        return map;
    }

    @GetMapping("orders/select")
    public Map queryOrder(@RequestParam(value = "customerName", required = false) String customerName, @RequestParam(value = "businessId", required = false) String businessId,
                          @RequestParam(value = "businessNumber", required = false) String businessNumber, @RequestParam(value = "businessManagerId", required = false) Integer businessManagerId,
                          @RequestParam(value = "orderNumber", required = false) String orderNumber, @RequestParam(value = "technicistId", required = false) Integer technicistId,
                          @RequestParam(value = "areaId", required = false) Integer areaId, @RequestParam(value = "orderStatus", required = false) String orderStatus,
                          @RequestParam(value = "orderStartTime", required = false) String orderStartTime, @RequestParam(value = "orderEndTime", required = false) String orderEndTime,
                          @RequestParam(value = "currentPage") Integer currentPage, @RequestParam(value = "pageSize") Integer pageSize, @RequestParam(value = "operatorId") Integer operatorId) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> list = new HashMap<>();
        Map<String, Object> dataMap = orderService.findOrderByPage(customerName, businessId, businessNumber, businessManagerId, orderNumber, technicistId, areaId, orderStatus, orderStartTime, orderEndTime, currentPage, pageSize, operatorId);
        if (dataMap == null) {
            map.put("status", "Not Found");
            map.put("message", "Order doesn't exist");
        } else {
            map.put("status", "OK");
            map.put("message", "Return Orders");
            list.put("list", dataMap.get("list"));
            list.put("total", dataMap.get("total"));
            map.put("data", list);
        }
        return map;
    }

    @GetMapping("/order/status/total")
    public Map getNumberOfEachStatusInCondition(@RequestParam(value = "customerName", required = false) String customerName, @RequestParam(value = "businessId", required = false) String businessId,
                                                @RequestParam(value = "businessNumber", required = false) String businessNumber, @RequestParam(value = "businessManagerId", required = false) Integer businessManagerId,
                                                @RequestParam(value = "orderNumber", required = false) String orderNumber, @RequestParam(value = "technicistId", required = false) Integer technicistId,
                                                @RequestParam(value = "areaId", required = false) Integer areaId, @RequestParam(value = "orderStatus", required = false) String orderStatus,
                                                @RequestParam(value = "orderStartTime", required = false) String orderStartTime, @RequestParam(value = "orderEndTime", required = false) String orderEndTime,
                                                @RequestParam(value = "operatorId") Integer operatorId) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> dataMap = orderService.getNumberOfEachStatusInCondition(customerName, businessId, businessNumber, businessManagerId, orderNumber, technicistId, areaId, orderStatus, orderStartTime, orderEndTime, operatorId);
        map.put("status", "OK");
        map.put("message", "Return number od each status");
        map.put("orderStatus", dataMap);
        return map;
    }

    @GetMapping("order/main/{operatorId}")
    public Map getOrderManageMainPage(@PathVariable int operatorId) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        map.put("message", "Return orders information and schedule");
        map.put("data", orderService.getOrdersInfoAndSchedule(operatorId));
        return map;
    }




}
