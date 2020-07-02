package com.springboot.crm.controller;

import com.springboot.crm.entity.Equipment;
import com.springboot.crm.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RequestMapping("/crm")
@RestController
public class EquipmentController {

    @Autowired
    EquipmentService equipmentService;

    @PostMapping("equipment/insert")
    public Map addEquipment(@RequestBody Equipment equipment){
        Map<String, Object> map = new HashMap<>();
        if(equipmentService.addEquipment(equipment)){
            map.put("status", "OK");
            map.put("message", "Add equipment successful");
        } else {
            map.put("status", "Exist");
            map.put("message", "Equipment already exist");
        }
        return map;
    }

    @GetMapping("equipment/select/{number}")
    public Map getEquipmentsByNumber(@PathVariable String number){
        Map<String, Object> map = new HashMap<>();
        Map data = equipmentService.getEquipmentsByNumber(number);
        int total = (int) data.get("total");
        if (total == 0){
            map.put("status", "Not Found");
            map.put("message", "Equipment doesn't exist");
        }else {
            map.put("status", "OK");
            map.put("message", "Return all equipments");
            Map[] maps = (Map[]) data.get("data");
            map.put("data", maps);
        }
        return map;
    }

    @PutMapping("equipment/update/{id}")
    public Map editEquipment(@PathVariable int id, @RequestBody Equipment equipment){
        Map<String, Object> map = new HashMap<>();
        if (equipmentService.editEquipment(id, equipment)){
            map.put("status", "OK");
            map.put("message", "Edit equipment successful");
        } else {
            map.put("status", "Exist");
            map.put("message", "Equipment already exist");
        }
        return map;
    }

    @GetMapping("equipment/detail/{id}")
    public Map getEquipmentInfoById(@PathVariable int id){
        Map<String, Object> map = new HashMap<>();
        Equipment data = equipmentService.getEquipmentInfo(id);
        map.put("status", "OK");
        map.put("message", "Return equipment information");
        map.put("data", data);
        return map;
    }

    @GetMapping("equipment/check/{modelNumber}")
    public Map checkDelete(@PathVariable String modelNumber){
        Map<String, Object> map = new HashMap<>();
        Map dataMap = equipmentService.checkDelete(modelNumber);
        String info = (String) dataMap.get("info");
        if (info == "Not Found"){
            map.put("status", "Not Found");
            map.put("message", "Equipment doesn't exist");
        } else if (info == "delete"){
            map.put("status", "OK");
            map.put("message", "Can be deleted");
            Equipment data = (Equipment) dataMap.get("data");
            map.put("data", data);
        } else {
            map.put("status", "Error");
            map.put("message", "Some equipments of this model still in storage ");
            Equipment data = (Equipment) dataMap.get("data");
            map.put("data", data);
        }
        return map;
    }

    @DeleteMapping("equipment/second_confirm/{id}/{operatorId}")
    public Map deleteEquipment(@PathVariable int id, @PathVariable int operatorId){
        Map<String, Object> map = new HashMap<>();
        equipmentService.deleteEquipment(id, operatorId);
        map.put("status", "OK");
        map.put("message", "Delete equipment successful");
        return map;
    }

    @GetMapping("equipment/mainPage")
    public Map mainPage(){
        Map<String, Object> map = new HashMap<>();
        Map[] data = equipmentService.getAllEquipments();
        map.put("status", "OK");
        map.put("message", "Return all the equipments");
        map.put("data", data);
        return map;
    }

    @GetMapping("equipment/index/{operatorId}")
    public Map getAllEquipmentInfo(@PathVariable int operatorId) {
        Map<String, Object> map = new HashMap<>();
        Map data = equipmentService.getAllEquipmentsInfo(operatorId);
        map.put("status", "OK");
        map.put("message", "Return all the equipments");
        map.put("data", data);
        return map;
    }



}
