package com.springboot.crm.controller;

import com.springboot.crm.entity.Type;
import com.springboot.crm.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.springboot.crm.constant.HttpCodes.SUCCESS;

//支持跨域
@CrossOrigin
@RestController
@RequestMapping("/crm/")
public class TypeController {

    @Autowired
    private TypeService typeService;

    //修改角色权限
    @PutMapping("type/{id}")
    public Map updateTypePermission(@PathVariable int id, @RequestBody Type type) {
        Map<String, Object> map = new HashMap<>();
        typeService.updateType(id, type);
        map.put("type", type);
        return map;
    }

    @GetMapping("common/getAllPermisson")
    public Map getAllPermissions() {
        Map<String, Object> map = new HashMap<>();
        Map[] data = typeService.getAllPermissions();
        map.put("status", "OK");
        map.put("data", data);
        return map;
    }


}
