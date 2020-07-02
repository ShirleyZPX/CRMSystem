package com.springboot.crm.controller;

import com.springboot.crm.entity.Business;
import com.springboot.crm.entity.BusinessPackage;
import com.springboot.crm.entity.LoadMode;
import com.springboot.crm.entity.TransactType;
import com.springboot.crm.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping("/crm")
@RestController
public class BusinessController {

    @Autowired
    BusinessService businessService;

    @PostMapping("business")
    public Map addBusiness(@RequestBody Business business) {
        Map<String, Object> map = new HashMap<>();
        if(businessService.addBusinessAndPackage(business)) {
            map.put("status", "OK");
            map.put("message", "Add business successful");
        } else {
            map.put("status", "Duplicated");
            map.put("message", "Business already exist");
        }
        return map;
    }


    @GetMapping("customerBusiness/package/{businessId}")
    public Map getPackages(@PathVariable int businessId) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        map.put("message", "Return all package information of this business");
        List<BusinessPackage> data = businessService.getPackages(businessId);
        map.put("data", data);
        return map;
    }

    @GetMapping("business/{name}/{currentPage}/{pageSize}")
    public Map getBusinessByName(@PathVariable String name, @PathVariable int currentPage, @PathVariable int pageSize) {
        Map<String, Object> map = new HashMap<>();
        Map[] data = businessService.getAllBusinessByName(name, currentPage, pageSize);
        if (data.length > 0) {
            map.put("status", "OK");
            map.put("message", "Return businesses");
            map.put("data", data);
        } else {
            map.put("status", "Error");
            map.put("message", "Businesses didn't exist");
        }
        return map;
    }

    @PutMapping("business/{id}")
    public Map editBusiness(@PathVariable int id, @RequestBody Business business) {
        Map<String, Object> map = new HashMap<>();
        if(businessService.editBusinessAndPackage(id, business)){
            map.put("status", "OK");
            map.put("message", "Edit business successful");
        } else {
            map.put("status", "Duplicated");
            map.put("message", "Business already exist");
        }
        return map;
    }

    @GetMapping("business/{name}")
    public Map checkBusinessDelete(@PathVariable String name) {
        Map<String, Object> map = new HashMap<>();
        String info = businessService.checkBusinessDelete(name);
        if (info == "error") { //business doesn't exist
            map.put("status", "Not Found");
            map.put("message", "Business doesn't exist");
        } else if (info == "success") {
            map.put("status", "OK");
            map.put("message", "Business can be deleted");
        } else {
            map.put("status", "Error");
            map.put("message", "Used in "+info+" modules");
        }
        return map;
    }

    @DeleteMapping("business/second-confirm/{name}")
    public Map deleteBusiness(@PathVariable String name, @RequestBody Map mapper) {
        Map<String, Object> map = new HashMap<>();
        int operatorId = (int) mapper.get("operatorId");
        businessService.deleteBusiness(name, operatorId);
        map.put("status", "OK");
        map.put("message", "Delete business successful");
        return map;
    }

    @GetMapping("businesses")
    public Map getAllBusinessInfo() {
        Map<String, Object> map = new HashMap<>();
        Map[] data = businessService.getAllBusinessInfo();
        map.put("status", "OK");
        map.put("message", "Return all businesses information");
        map.put("data", data);
        return map;
    }

    @GetMapping("business/getByOperatoId")
    public Map getBusinessByOperator(@RequestParam(value = "operatorId") Integer operatorId, @RequestParam(value = "currentPage", required = false) Integer currentPage, @RequestParam(value = "pageSize", required = false) Integer pageSize){
        Map<String, Object> map = new HashMap<>();
        Map data = businessService.getBusinessByOperatorIdBypage(operatorId, currentPage, pageSize);
        map.put("status", "OK");
        map.put("message", "Return all businesses");
        map.put("data", data);
        return map;
    }

    @GetMapping("business/getByAreaId/{areaId}")
    public Map getBusinessByArea(@PathVariable int areaId){
        Map<String, Object> map = new HashMap<>();
        Map[] data  = businessService.getBusinessByAreaId(areaId);
        map.put("status", "OK");
        map.put("message", "Return areas of the operator");
        map.put("data", data);
        return map;
    }

    @PostMapping("business/loadMode")
    public Map addLoadMode(@RequestBody LoadMode loadMode) {
        Map<String, Object> map = new HashMap<>();
        if(businessService.addLoadMode(loadMode)) {
            map.put("status", "OK");
            map.put("message", "Add load mode successful");
        } else {
            map.put("status", "Exist");
            map.put("message", "Load mode already exist");
        }
        return map;
    }

    @GetMapping("business/loadMode")
    public Map getLoadMode() {
        Map<String, Object> map = new HashMap<>();
        List<LoadMode> data  = businessService.getLoadMode();
        map.put("status", "OK");
        map.put("message", "Return load mode");
        map.put("data", data);
        return map;
    }

    @PostMapping("business/transactType")
    public Map addTransactType(@RequestBody TransactType transactType) {
        Map<String, Object> map = new HashMap<>();
        if(businessService.addTransactType(transactType)) {
            map.put("status", "OK");
            map.put("message", "Add transact type successful");
        } else {
            map.put("status", "Exist");
            map.put("message", "Transact type already exist");
        }
        return map;
    }

    @GetMapping("business/transactType")
    public Map getTransactType() {
        Map<String, Object> map = new HashMap<>();
        List<TransactType> data  = businessService.getTransactType();
        map.put("status", "OK");
        map.put("message", "Return all transact type");
        map.put("data", data);
        return map;
    }

}
