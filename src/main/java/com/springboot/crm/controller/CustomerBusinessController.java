package com.springboot.crm.controller;

import com.springboot.crm.entity.CustomerBusiness;
import com.springboot.crm.service.CustomerBusinessService;
import com.springboot.crm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RequestMapping("/crm")
@RestController
public class CustomerBusinessController {
    @Autowired
    CustomerBusinessService customerBusinessService;
    @Autowired
    OrderService orderService;

    @GetMapping("customerBusiness/main")
    public Map mainPage(){
        Map<String, Object> map = new HashMap<>();
        Map[] data = customerBusinessService.getMainPage();
        map.put("status", "OK");
        map.put("message", "Return all customer business info");
        map.put("data", data);
        return map;
    }

    @GetMapping("customerBusiness/{id}/{customerName}/{businessName}")
    public Map findBusinessByName(@PathVariable int id, @PathVariable String customerName, @PathVariable String businessName){
        Map<String, Object> map = new HashMap<>();
        Map[] data = customerBusinessService.findBusinessByName(id, customerName, businessName);
        if (data.length == 0) {
            map.put("status", "Not Found");
            map.put("message", "Customer business doesn't exist");
        } else {
            map.put("status", "OK");
            map.put("message", "Return all customer business information");
            map.put("data", data);
        }
        return map;
    }

    @PostMapping("customerBusiness")
    public Map addCustomerBusiness(@RequestBody CustomerBusiness customerBusiness){
        Map<String, Object> map = new HashMap<>();
        if(customerBusinessService.addCustomerBusiness(customerBusiness)){
            map.put("status", "OK");
            map.put("message", "Add customer business successful");
        } else {
            map.put("status", "Error");
            map.put("message", "Customer manager not found");
        }
        return map;
    }

    @PutMapping("customerBusiness/{id}")
    public Map editCustomerBusiness(@PathVariable int id, @RequestBody CustomerBusiness customerBusiness){
        Map<String, Object> map = new HashMap<>();
        if (customerBusinessService.editCustomerBusiness(id, customerBusiness)){
            map.put("status", "OK");
            map.put("message", "Edit customer business successful");
        } else {
            map.put("status", "Error");
            map.put("message", "Customer manager not found");
        }
        return map;
    }

    @GetMapping("customerBusiness/{id}")
    public Map getCustomerBusinessInfo(@PathVariable int id){
        Map<String, Object> map = new HashMap<>();
        Map data = orderService.getOrderInfoById(id, 0);
        map.put("status", "OK");
        map.put("message", "Return customer business info");
        map.put("data", data);
        return map;
    }

    @GetMapping("customerBusiness/delete/{id}")
    public Map checkDelete(@PathVariable int id){
        Map<String, Object> map = new HashMap<>();
        Map[] data = customerBusinessService.checkDelete(id);
        if (data.length == 0) {
            map.put("status", "OK");
            map.put("message", "Can be deleted");
        } else {
            map.put("status", "Error");
            map.put("message", "Can not be deleted");
            map.put("data", data);
        }
        return map;
    }

    @DeleteMapping("customerBusiness/{id}")
    public Map deleteBusiness(@PathVariable int id){
        Map<String, Object> map = new HashMap<>();
        customerBusinessService.deleteCustomerBusiness(id);
        map.put("status", "OK");
        map.put("message", "Delete customer business successful");
        return map;
    }

    @GetMapping("customerBusiness/manager/{currentPage}/{pageSize}")
    public Map getUnassignedBusiness(@PathVariable int currentPage, @PathVariable int pageSize){
        Map<String, Object> map = new HashMap<>();
        Map[] data = customerBusinessService.getUnassignedBusiness(currentPage, pageSize);
        map.put("status", "OK");
        map.put("message", "Return all customers with no business manager");
        map.put("data", data);
        return map;
    }

    @PutMapping("customerBusiness/manager/{id}")
    public Map assignBusiness(@PathVariable int id, @RequestBody Map mapper){
        int businessManagerId = (int) mapper.get("businessManagerId");
        int operatorId = (int) mapper.get("operatorId");
        Map<String, Object> map = new HashMap<>();
        customerBusinessService.assignBusiness(id, businessManagerId, operatorId);
        map.put("status", "OK");
        map.put("message", "Set business manager successful");
        return map;
    }

    @GetMapping("customerBusiness")
    public Map findCustomerBusiness(@RequestParam(value = "customerName", required = false) String customerName, @RequestParam(value = "businessName", required = false) String businessName,
                                    @RequestParam(value = "status", required = false) String status, @RequestParam(value = "installAddress", required = false) String  installAddress,
                                    @RequestParam(value = "orderNumber", required = false) String orderNumber, @RequestParam(value = "orderType", required = false) String orderType,
                                    @RequestParam(value = "orderAcceptTime", required = false) String orderAcceptTime, @RequestParam(value = "orderReturnTime", required = false) String orderReturnTime,
                                    @RequestParam(value = "loadMode", required = false) String loadMode, @RequestParam(value = "businessNumber", required = false) String businessNumber,
                                    @RequestParam(value = "bandWidth", required = false) String bandWidth, @RequestParam(value = "unicomCustomerId", required = false) String unicomCustomerId,
                                    @RequestParam(value = "transactType", required = false) String transactType, @RequestParam(value = "packageNameId", required = false) Integer packageNameId,
                                    @RequestParam(value = "customerManager", required = false) String customerManager, @RequestParam(value = "managerPhone", required = false) String managerPhone,
                                    @RequestParam(value = "businessManagerId", required = false) Integer businessManagerId,@RequestParam(value = "orderStatus", required = false) String orderStatus,
                                    @RequestParam(value = "currentPage") Integer currentPage, @RequestParam(value = "pageSize") Integer pageSize) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        Map[] data = customerBusinessService.findCustomerBusiness(customerName, businessName, status, installAddress, orderNumber, orderType, orderAcceptTime, orderReturnTime, loadMode, businessNumber, bandWidth, unicomCustomerId, transactType, packageNameId, customerManager, managerPhone, businessManagerId, orderStatus, currentPage, pageSize);
        if (data != null || data.length > 0){
            map.put("status", "OK");
            map.put("message", "Return customers");
            map.put("data", data);
        } else {
            map.put("status", "Not Found");
            map.put("message", "Customer business doesn't exist");
        }
        return map;
    }

    @GetMapping("customerBusiness/find/{businessNumber}")
    public Map findInfoByBusinessNumber(@PathVariable String businessNumber){
        Map<String, Object> map = new HashMap<>();
        Map data = customerBusinessService.findInfoByBusinessNumber(businessNumber);
        map.put("status", "OK");
        map.put("message", "Return all businesses information");
        map.put("data", data);
        return map;
    }

    @RequestMapping("customerBusiness/download")
    public void customerBusinessInfoExport(HttpServletResponse response) throws IOException {
        customerBusinessService.exportCustomerBusinessInfos(response);
    }

    @RequestMapping("customerBusiness/template")
    public void downloadTemplate(HttpServletResponse response) throws FileNotFoundException, UnsupportedEncodingException {
        customerBusinessService.downloadTemplate(response);
    }


    @PostMapping("customerBusiness/template/upload")
    public Map uploadCustomerBusinessInfo(@RequestBody CustomerBusiness customerBusiness) {
        Map<String, Object> map = new HashMap<>();
        Map data = customerBusinessService.uploadCustomerBusinessInfo(customerBusiness);
        String info = (String) data.get("info");
        if (info.equals("add")) {
            map.put("status", "OK");
            map.put("message", "Successfully added");
        } else if (info.equals("Customer Manager not found")) {
            map.put("status", "Error");
            map.put("message", "Customer Manager not found");
        } else if (info.equals("Business Manager not found")) {
            map.put("status", "Error");
            map.put("message", "Business Manager not found");
        } else if (info.equals("Business not found")){
            map.put("status", "Error");
            map.put("message", "Business not found");
        } else if (info.equals("Customer not found")) {
            map.put("status", "Error");
            map.put("message", "Customer not found");
        } else {
            map.put("status", "Exist");
            map.put("message", "Has already exist");
            map.put("data", data);
        }
        return map;

    }


}
