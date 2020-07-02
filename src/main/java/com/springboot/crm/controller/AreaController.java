package com.springboot.crm.controller;

import com.springboot.crm.entity.Area;
import com.springboot.crm.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


//支持跨域
@CrossOrigin
@RequestMapping("/crm")
@RestController
public class AreaController {

    @Autowired
    private AreaService areaService;

    //新增区域
    @PostMapping("/area")
    public Map insertArea(@RequestBody Area area) {
        Map<String, Object> map = new HashMap<>();
        if(areaService.insertArea(area)){
            map.put("status", "OK");
            map.put("message", "Add area successful");
        } else {
            map.put("status", "Duplicated");
            map.put("message", "Area already exist");
        }
        return map;

    }

    //修改区域
    @PutMapping("/area/{id}")
    public Map updateArea(@PathVariable int id, @RequestBody Area area) {
        Map<String, Object> map = new HashMap<>();
        if(areaService.editArea(id, area)) {
            map.put("status", "OK");
            map.put("message", "Edit area successful");
        } else {
            map.put("status", "Duplicated");
            map.put("message", "Area already exist");
        }
        return map;
    }

    //删除区域
    @DeleteMapping("/area/{areaName}/{areaCode}")
    public Map deleteArea(@PathVariable String areaName, @PathVariable String areaCode) {
        Map<String, Object> map = new HashMap<>();
        String info = areaService.deleteArea(areaName, areaCode);
        if (info == "") {
            map.put("status", "OK");
            map.put("message", "Delete area successful");
        } else if(info == "error"){
            map.put("status", "Error");
            map.put("message", "Area doesn't exist");
        } else {
            map.put("status", "Error");
            map.put("message", "Used in "+info+" modules");
        }
        return map;
    }

    //获取所有区域
    @GetMapping("/areas")
    public Map getAllAreas() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        map.put("message", "Return all areas successful");
        map.put("data", areaService.getAllAreas());
        return map;
    }

    //根据登录账号获取区域
    @GetMapping("/area/getAreaByOperatorId/{operatorId}")
    public Map getAreaByOperatorId(@PathVariable int operatorId) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        map.put("message", "Return areas of the operator");
        map.put("data", areaService.getAreasByOperatorId(operatorId));
        return map;
    }
}
