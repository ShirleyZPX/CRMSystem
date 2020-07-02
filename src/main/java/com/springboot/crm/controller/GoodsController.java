package com.springboot.crm.controller;

import com.springboot.crm.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//支持跨域
@CrossOrigin
@RestController
@RequestMapping("/crm/")
public class GoodsController {
    @Autowired
    GoodsService goodsService;

    @PostMapping("goods/inStorage")
    public Map inStoreGoods(@RequestBody Map mapper) throws ParseException {
        String id = (String) mapper.get("id");
        String buyDate = (String) mapper.get("buyDate");
        String inStorageTime = (String) mapper.get("inStorageTime");
        String inStoragePeopleId = (String) mapper.get("inStoragePeopleId");
        String hardwareCode = (String) mapper.get("hardwareCode");
        String areaId = (String) mapper.get("areaId");
        List<Map> deviceCodes = (List<Map>) mapper.get("deviceCodes");
        String operatorId = (String) mapper.get("operatorId");
        Map<String, Object> map = new HashMap<>();
        String info = goodsService.inStoreGoods(id, buyDate, inStoragePeopleId, inStorageTime, hardwareCode, areaId, deviceCodes, operatorId);
        if (info.equals("")) {
            map.put("status", "OK");
            map.put("message", "Add in storage successful");
        } else {
            map.put("status", "Error");
            map.put("message", info + "device code exist");
        }
        return map;
    }

    @GetMapping("goods")
    public  Map checkRecycleGoods(@RequestParam(required = false) String deviceCode, @RequestParam(required = false) String deviceNumber) {
        Map<String, Object> map = new HashMap<>();
        Map info = goodsService.checkRecycleGoodsByDeviceCode(deviceCode, deviceNumber);
        if (info.get("info").equals("not found")) {
            map.put("status", "Not Found");
            map.put("message", "Device doesn't exist");
        } else if (info.get("info").equals("store")) {
            map.put("status", "Error");
            map.put("message", "Already in storage");
        } else {
            map.put("status", "OK");
            map.put("message", "Can be re-stored");
            map.put("data", info.get("data"));
        }
        return map;
    }

    @PutMapping("goods/recycle/{id}/{operatorId}")
    public Map recycleGoods(@PathVariable int id, @PathVariable int operatorId) {
        Map<String, Object> map = new HashMap<>();
        goodsService.recycleGoods(id, operatorId);
        map.put("status", "OK");
        map.put("message", "Re-stored successful");
        return map;
    }

    @GetMapping("goods/out")
    public Map checkoutStoreGoods(@RequestParam String deviceNumber, @RequestParam String deviceCode, @RequestParam Integer collectedPeopleId) {
        Map<String, Object> map = new HashMap<>();
        Map info = goodsService.checkOutStoreGoods(deviceCode, deviceNumber, collectedPeopleId);
        String mes = (String) info.get("info");
        if (mes.equals("not found")) {
            map.put("status", "Not Found");
            map.put("message", "Device doesn't exist");
        } else if (mes.equals("error")) {
            map.put("status", "Error");
            map.put("message", "Device already out of storage");
        } else {
            map.put("status", "OK");
            map.put("message", "Return all devices information");
            map.put("data", info.get("data"));
            map.put("id", info.get("id"));
            map.put("deviceNumber", info.get("deviceNumber"));
            map.put("deviceCode", info.get("deviceCode"));
            map.put("deviceType", info.get("deviceType"));
        }
        return map;
    }

    @PutMapping("goods/out/{id}/{operatorId}")
    public Map outStoreGoods(@PathVariable int id, @PathVariable int operatorId) {
        Map<String, Object> map = new HashMap<>();
        goodsService.outStoreGoods(id, operatorId);
        map.put("status", "OK");
        map.put("message", "Device out of storage successful");
        return map;
    }

    @GetMapping("goods/transfer/{id}/{areaId1}/{areaId2}")
    public Map getTransferGoods(@PathVariable int id, @PathVariable int areaId1, @PathVariable int areaId2) {
        Map<String, Object> map = new HashMap<>();
        List<Map> data = goodsService.getTransferGoods(id, areaId1, areaId2);
        map.put("status", "OK");
        map.put("message", "Return all devices");
        map.put("data", data);
        return map;
    }

    @PutMapping("goods/transfer")
    public Map transferGoods(@RequestBody Map mapper) {
        List<Integer> ids = (List<Integer>) mapper.get("id");
        int areaId1 = (int) mapper.get("areaId1");
        int areaId2 = (int) mapper.get("areaId2");
        String operatorId = (String) mapper.get("operatorId");
        Map<String, Object> map = new HashMap<>();
        goodsService.transferGoods(ids, areaId1, areaId2, operatorId);
        map.put("status", "OK");
        map.put("message", "Change area successful");
        return map;
    }

    @GetMapping("goods/check/{id}/{areaId}")
    public Map getCheckGoodsInfo(@PathVariable int id, @PathVariable int areaId) {
        Map<String, Object> map = new HashMap<>();
        List<Map> data = goodsService.getCheckGoodsInfo(id, areaId);
        map.put("status", "OK");
        map.put("message", "Return all devices");
        map.put("data", data);
        return map;
    }

    @PostMapping("goods/record")
    public Map recordGoods(@RequestBody Map mapper) {
        int id = (int) mapper.get("id");
        String status = (String) mapper.get("status");
        Map<String, Object> map = new HashMap<>();
        goodsService.recordGoods(id, status);
        map.put("status", "OK");
        map.put("message", "Add result successful");
        return map;
    }

    @GetMapping("goods/records")
    public Map getGoodsRecords(@RequestParam Boolean unusual, @RequestParam Integer areaId) {
        Map<String, Object> map = new HashMap<>();
        List<Map> data = goodsService.getGoodsRecords(unusual, areaId);
        map.put("status", "OK");
        map.put("message", "Return all abnormal records");
        map.put("data", data);
        return map;
    }

    @GetMapping("goods/info/{deviceNumber}")
    public Map getGoodsInfo(@PathVariable String deviceNumber) {
        Map<String, Object> map = new HashMap<>();
        Map data = goodsService.getGoodsInfoByDeviceNum(deviceNumber);
        if (data == null) {
            map.put("status", "Not Found");
            map.put("message", "Device doesn't exist");
        } else {
            map.put("status", "OK");
            map.put("message", "Return device information");
            map.put("data", data);
        }
        return map;
    }

    @PutMapping("good/{id}")
    public Map editGoods(@PathVariable int id, @RequestBody Map mapper) throws ParseException {
        String modelNumber = (String) mapper.get("modelNumber");
        String status = (String) mapper.get("status");
        String deviceNumber = (String) mapper.get("deviceNumber");
        String buyDate = (String) mapper.get("buyDate");
        String inStorageTime = (String) mapper.get("inStorageTime");
        int inStoragePeopleId = (int) mapper.get("inStoragePeopleId");
        String outStorageTime = (String) mapper.get("outStorageTime");
        int outStoragePeopleId = (int) mapper.get("outStoragePeopleId");
        String hardwareCode = (String) mapper.get("hardwareCode");
        int areaId = (int) mapper.get("areaId");
        String deviceCode = (String) mapper.get("deviceCode");
        String operatorId = (String) mapper.get("operatorId");
        Map<String, Object> map = new HashMap<>();
        goodsService.editGoods(id, modelNumber, status, deviceNumber,  buyDate, inStorageTime, inStoragePeopleId, outStorageTime, outStoragePeopleId, hardwareCode, areaId, deviceCode, operatorId);
        map.put("status", "OK");
        map.put("message", "Changes commit successful");
        return map;
    }

    @GetMapping("goods/change/apply")
    public Map checkChangesList(@RequestParam Integer pageSize, @RequestParam Integer currentPage) {
        Map<String, Object> map = new HashMap<>();
        Map data = goodsService.checkChangesList(currentPage, pageSize);
        map.put("status", "OK");
        map.put("message", "Return all changes that need check");
        map.put("data", data);
        return map;
    }

    @GetMapping("good/change/{id}")
    public Map checkSpecificGoodChanges(@PathVariable int id) {
        Map<String, Object> map = new HashMap<>();
        Map data = goodsService.getSpecificGoodChanges(id);
        map.put("status", "OK");
        map.put("message", "Return changes of the device");
        map.put("old", data.get("old"));
        map.put("new", data.get("new"));
        return map;
    }

    @PutMapping("good/change/result/{id}")
    public Map changeGoodSuggestion(@PathVariable int id, @RequestBody Map mapper) {
        Boolean result = (Boolean) mapper.get("result");
        int operatorId = Integer.parseInt((String) mapper.get("operatorId"));
        Map<String, Object> map = new HashMap<>();
        goodsService.changeGoodSuggest(id, result, operatorId);
        map.put("status", "OK");
        map.put("message", "Get the changes result successful");
        return map;
    }

    @GetMapping("goods/selectByParams")
    public Map findGoods(@RequestParam(value = "modelNumber", required = false) String modelNumber, @RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "deviceNumber", required = false) String deviceNumber, @RequestParam(value = "buyDate", required = false) String  buyDate,
                         @RequestParam(value = "customerName", required = false) String customerName, @RequestParam(value = "inStorageTime", required = false) String inStorageTime,
                         @RequestParam(value = "inStoragePeopleId", required = false) Integer inStoragePeopleId, @RequestParam(value = "outStorageTime", required = false) String outStorageTime,
                         @RequestParam(value = "outStoragePeopleId", required = false) Integer outStoragePeopleId, @RequestParam(value = "deviceCode", required = false) String deviceCode,
                         @RequestParam(value = "hardwareCode", required = false) String hardwareCode, @RequestParam(value = "areaId", required = false) Integer areaId,
                         @RequestParam(value = "currentPage") Integer currentPage, @RequestParam(value = "pageSize") Integer pageSize) throws ParseException {

        Map<String, Object> map = new HashMap<>();
        Map data = goodsService.findGoods(modelNumber, status, deviceNumber, buyDate, customerName, inStorageTime, inStoragePeopleId, outStorageTime, outStoragePeopleId, deviceCode, hardwareCode, areaId, currentPage, pageSize);
        if (data == null) {
            map.put("status", "Not Found");
            map.put("message", "Device doesn't exist");
        } else {
            map.put("status", "OK");
            map.put("message", "Return device information");
            map.put("data", data);
        }
        return map;
    }


}
