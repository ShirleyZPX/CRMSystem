package com.springboot.crm.controller;

import com.springboot.crm.entity.Customer;
import com.springboot.crm.service.CustomerService;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RequestMapping("/crm/")
@RestController
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping("customer")
    public Map addCustomer(@RequestBody Customer customer){
        Map<String, Object> map = new HashMap<>();
        if (customerService.addCustomer(customer)){ // add successful
            map.put("status", "OK");
            map.put("message", "Add customer successful");
        } else {
            map.put("status", "Error");
            map.put("message", "Customer manager doesn't exist");
        }
        return map;
    }

    @GetMapping("customer/{name}/{operatorId}")
    public Map checkCustomerAdd(@PathVariable String name, @PathVariable int operatorId){
        Map<String, Object> map = new HashMap<>();
        Customer customer = customerService.checkCustomerAdd(name, operatorId);
        if (customer == null){
            map.put("status", "OK");
            map.put("message", "Create");
        } else {
            map.put("status", "OK");
            map.put("message", "Customer exist");
            map.put("data", customer);
        }
        return map;
    }

    @GetMapping("customers/{name}/{id}/{currentPage}/{pageSize}")
    public Map getCustomersByNameLike(@PathVariable String name, @PathVariable int id, @PathVariable int currentPage, @PathVariable int pageSize) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        Map[] data = customerService.getCustomersByNameLikeByPage(name, id, currentPage, pageSize);
        if (data.length == 0) {
            map.put("status", "Not Found");
            map.put("message", "Customer doesn't exist");
        } else {
            map.put("status", "OK");
            map.put("message", "Return customers");
            map.put("data", data);
        }
        return map;
    }

    @GetMapping("customers/all/{name}/{id}/{currentPage}/{pageSize}")
    public Map getAllCustomersByNameLike(@PathVariable String name, @PathVariable int id, @PathVariable int currentPage, @PathVariable int pageSize){
        Map<String, Object> map = new HashMap<>();
        Map[] data = customerService.getAllCustomersByNameLikeByPage(name, id, currentPage, pageSize);
        if (data.length == 0) {
            map.put("status", "Not Found");
            map.put("message", "Customer doesn't exist");
        } else {
            map.put("status", "OK");
            map.put("message", "Return customers");
            map.put("data", data);
        }
        return map;
    }

    @GetMapping("customer/{id}")
    public Map getCustomerInfo(@PathVariable int id) {
        Map<String, Object> map = new HashMap<>();
        Map data = customerService.getCustomerInfoById(id);
        map.put("status", "OK");
        map.put("message", "Customer information");
        map.put("data", data);
        return map;
    }

    @PutMapping("customer/{id}")
    public Map editCustomer(@PathVariable int id, @RequestBody Customer customer){
        Map<String, Object> map = new HashMap<>();
        if(customerService.editCustomer(id, customer)){
            map.put("status", "OK");
            map.put("message", "Edit customer successful");
        } else {
            map.put("status", "Error");
            map.put("message", "Customer manager doesn't exist");
        }
        return map;
    }

    @GetMapping("customer/manager/{areaId}")
    public Map getBusinessManagerByArea(@PathVariable int areaId){
        Map<String, Object> map = new HashMap<>();
        Map[] data = customerService.getBusinessManagerByArea(areaId);
        map.put("status", "OK");
        map.put("message", "Return business managers");
        map.put("data", data);
        return map;
    }

    @GetMapping("customer/delete/{id}")
    public Map checkCustomerDelete(@PathVariable int id){
        Map<String, Object> map = new HashMap<>();
        Map data = customerService.checkCustomerDelete(id);
        boolean status = (boolean) data.get("delete");
        if (status){ //can be delete
            map.put("status", "OK");
            map.put("message", "Customer can be deleted");
        } else {
            map.put("status", "Error");
            map.put("message", "Can't be deleted, has related business");
        }
        map.put("data", data);
        return map;
    }

    @DeleteMapping("customer/confirm/{id}/{operatorId}")
    public Map deleteCustomer(@PathVariable int id, @PathVariable int operatorId){
        Map<String, Object> map = new HashMap<>();
        customerService.deleteCustomer(id, operatorId);
        map.put("status", "OK");
        map.put("message", "Delete customer successful");
        return map;
    }

    @GetMapping("customer/businessManager/list/{operatorId}/{currentPage}/{pageSize}")
    public Map getUnclaimedList(@PathVariable int operatorId, @PathVariable int currentPage, @PathVariable int pageSize){
        Map<String, Object> map = new HashMap<>();
        Map[] data = customerService.getUnclaimedList(operatorId, currentPage, pageSize);
        map.put("status", "OK");
        map.put("message", "Return all customers with no business manager");
        map.put("data", data);
        return map;
    }

    @PutMapping("customer/businessManager/{id}")
    public Map claimCustomer(@PathVariable int id, @RequestBody Map mapper){
        Map<String, Object> map = new HashMap<>();
        int managerId = (int) mapper.get("businessManagerId");
        int operatorId = (int) mapper.get("operatorId");
        customerService.claimCustomer(id, managerId, operatorId);
        map.put("status", "OK");
        map.put("message", "Set business manager successful");
        return map;
    }

    @GetMapping("customer/follow/{id}")
    public Map getCustomerFollowRecord(@PathVariable int id){
        Map<String, Object> map = new HashMap<>();
        Map data  = customerService.getCustomerFollowRecord(id);
        map.put("status", "OK");
        map.put("message", "Return all follow records");
        map.put("data", data);
        return map;
    }

    @PostMapping("customer/follow")
    public Map addFollowRecord(@RequestBody Map mapper) throws ParseException {
        int id = (int) mapper.get("id");
        String text = (String) mapper.get("followText");
        String date = (String) mapper.get("date");
        String file = (String) mapper.get("followFile");
        String type = (String) mapper.get("fileType");
        int operatorId = (int) mapper.get("operatorId");
        Map<String, Object> map = new HashMap<>();
        customerService.addFollowRecord(id, text, date, file, type, operatorId);
        map.put("status", "OK");
        map.put("message", "Add follow record successful");
        return map;
    }

    @GetMapping("customer")
    public Map findCustomer(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "previousName", required = false) String previousName,
                            @RequestParam(value = "address", required = false) String address, @RequestParam(value = "areaId", required = false) Integer areaId,
                            @RequestParam(value = "contactPerson", required = false) String contactPerson, @RequestParam(value = "phone1", required = false) String phone1,
                            @RequestParam(value = "phone2", required = false) String phone2, @RequestParam(value = "customerManager", required = false) String customerManager,
                            @RequestParam(value = "managerPhone", required = false) String managerPhone, @RequestParam(value = "businessManagerId", required = false) Integer businessManagerId,
                            @RequestParam(value = "currentPage") Integer currentPage, @RequestParam(value = "pageSize") Integer pageSize){
        Map<String, Object> map = new HashMap<>();
        Map[] data = customerService.findCustomer(name, previousName, address, areaId, contactPerson, phone1, phone2, customerManager, managerPhone, businessManagerId, currentPage, pageSize);
        if (data != null || data.length > 0){
            map.put("status", "OK");
            map.put("message", "Return all customers");
            map.put("data", data);
        } else {
            map.put("status", "Not Found");
            map.put("message", "Customer doesn't exist");
        }
        return map;
    }

    @GetMapping("customer/info/{id}")
    public Map getCustomerInfoWhenFind(@PathVariable int id){
        Map<String, Object> map = new HashMap<>();
        Map data = customerService.getCustomerInfoWhenFind(id);
        map.put("status", "OK");
        map.put("message", "Return customer information");
        map.put("data", data);
        return map;
    }

    @GetMapping("customers")
    public Map getAllUndeleteCustomers(){
        Map<String, Object> map = new HashMap<>();
        Map[] data = customerService.getAllUndeleteCustomers();
        map.put("status", "OK");
        map.put("message", "Return all customers");
        map.put("data", data);
        return map;
    }

    @GetMapping("customer/main/{operatorId}")
    public Map getCustomerMainPage(@PathVariable int operatorId){
        Map<String, Object> map = new HashMap<>();
        Map[] data = customerService.getMainPage(operatorId);
        map.put("status", "OK");
        map.put("message", "Return customers");
        map.put("data", data);
        return map;
    }

    @GetMapping("customer/download/info/{id}")
    public void downloadCustomerInfo(HttpServletResponse response, @PathVariable int id) throws IOException {
        customerService.downloadCustomerInfo(response, id);
    }

    @GetMapping("customers/download/info")
    public void downloadAllCustomersInfo(HttpServletResponse response) throws IOException {
        customerService.downloadAllCustomers(response);
    }

    @GetMapping("customer/template")
    public void downloadTemplate(HttpServletResponse response) throws UnsupportedEncodingException, FileNotFoundException {
        customerService.downloadTemplate(response);
    }

    @PutMapping("customer/upload/info")
    public Map uploadCustomerInfo(@RequestBody Customer customer) {
        Map<String, Object> map = new HashMap<>();
        Map data = customerService.uploadCustomerInfo(customer);
        String info = (String) data.get("info");
        if (info.equals("add")) {
            map.put("status", "OK");
            map.put("message", "Add customer successful");
        } else if (info.equals("Area not found")) {
            map.put("status", "Error");
            map.put("message", "Area not found");
        } else if (info.equals("Customer Manager not found")) {
            map.put("status", "Error");
            map.put("message", "Customer Manager not found");
        } else if (info.equals("Business Manager not found")) {
            map.put("status", "Error");
            map.put("message", "Business Manager not found");
        } else {
            map.put("status", "Exist");
            map.put("message", "Has already have this customer");
            map.put("data", data);
        }
        return map;
    }


}
