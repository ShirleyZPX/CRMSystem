package com.springboot.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.crm.dao.CheckGoodsRecordsMapper;
import com.springboot.crm.dao.EditGoodsBeforeMapper;
import com.springboot.crm.dao.GoodsMapper;
import com.springboot.crm.entity.*;
import com.springboot.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    CustomerService customerService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    AreaService areaService;
    @Autowired
    EditGoodsService editGoodsService;
    @Autowired
    CheckGoodsRecordsMapper checkGoodsRecordsMapper;
    @Autowired
    EditGoodsBeforeMapper editGoodsBeforeMapper;


    private final static String IN_STORAGE = "库存";
    private final static String OUT_STORAGE = "出库";
    private final static String UNUSUAL = "库存异常";

    @Override
    public Goods getGoodsByGoodsCode(String goodsCode) {
        GoodsExample example = new GoodsExample();
        GoodsExample.Criteria criteria = example.createCriteria();
        criteria.andNumberEqualTo(goodsCode);
        List<Goods> list = goodsMapper.selectByExample(example);
        if (list.size() == 0) {
            return null;
        } else {
            Goods goods = list.get(0);
            return goods;
        }
    }

    @Override
    public boolean checkEquipmentInStorage(Equipment equipment) {
        int equipmentId = equipment.getId();
        GoodsExample example = new GoodsExample();
        GoodsExample.Criteria criteria = example.createCriteria();
        criteria.andEquipmentIdEqualTo(equipmentId); //get all the goods of this equipment
        criteria.andStatusEqualTo(IN_STORAGE);
        long number = goodsMapper.countByExample(example);
        if (number > 0){
            return true;
        }
        return false;
    }

    @Override
    public Goods getGoodById(Integer id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public String getModelNumber(Goods good) {
        int equipId = good.getEquipmentId();
        Equipment equipment = equipmentService.getEquipmentById(equipId);
        return equipment.getModelNumber();
    }

    @Override
    public int getStoreGoodsNumByEquipment(Equipment equipment) {
        GoodsExample example = new GoodsExample();
        GoodsExample.Criteria criteria = example.createCriteria();
        criteria.andEquipmentIdEqualTo(equipment.getId());
        criteria.andStatusEqualTo(IN_STORAGE);
        return goodsMapper.selectByExample(example).size();
    }

    @Override
    public String inStoreGoods(String id, String buyDate, String inStoragePeopleId, String inStorageTime, String hardwareCode, String areaId, List<Map> deviceCodes, String operatorId) throws ParseException {
        String info="";
        int total = getTotalGoodsNumByEquipment(equipmentService.getEquipmentById(Integer.parseInt(id))); //get the total num of this kind of goods
        System.out.println(total);
        int num = 0;
        for (int i=0; i<deviceCodes.size(); i++) {
            Map map = deviceCodes.get(i);
            String deviceCode = (String) map.get("value"); //get the device code of each goods
            Goods good = checkGoodsExistByDeviceCode(deviceCode);
            if (good != null) { //goods exist
                info += "No." + i +" ";
            } else { // not exist
                Goods goods = new Goods();
                goods.setEquipmentId(Integer.parseInt(id));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                goods.setPurchaseTime(format.parse(buyDate));
                goods.setInStorageTime(format.parse(inStorageTime));
                goods.setInStoragePeopleId(Integer.parseInt(inStoragePeopleId));
                goods.setHardwareCode(hardwareCode);
                goods.setStorageAreaId(Integer.parseInt(areaId));
                goods.setDeviceCode(deviceCode);
                goods.setOperatorId(Integer.parseInt(operatorId));
                goods.setCreateTime(new Date());
                goods.setStatus(IN_STORAGE);
                int number = total+ num;
                goods.setNumber(equipmentService.getEquipmentById(Integer.parseInt(id)).getModelNumber()+number);
                num++;
                goodsMapper.insert(goods);
            }
        }
        return info;
    }

    @Override
    public Map checkRecycleGoodsByDeviceCode(String deviceCode, String deviceNumber) {
        Map data = new HashMap();
        Goods good = checkGoodsByDevice(deviceCode, deviceNumber);
        if (good == null) {
            data.put("info", "not found");
        } else {
            if (good.getStatus().equals(IN_STORAGE)) {
                data.put("info", "store");
            } else {
                data.put("info", "ok");
                Map map = getGoodsInfo(good);
                data.put("data", map);
            }
        }
        return data;
    }

    @Override
    public void recycleGoods(int id, int operatorId) {
        Goods goods = getGoodById(id);
        editGoodsService.addRecord(goods, operatorId);
        goods.setStatus(IN_STORAGE);
        goods.setUsingCustomerId(null);
        goods.setInStoragePeopleId(operatorId);
        goods.setInStorageTime(new Date());
        goods.setOutStoragePeopleId(null);
        goods.setOutStorageTime(null);
        goods.setEditOperaterId(operatorId);
        goods.setEditTime(new Date());
        goodsMapper.updateByPrimaryKeySelective(goods);
    }

    @Override
    public Map checkOutStoreGoods(String deviceCode, String deviceNumber, Integer collectedPeopleId) {
        Goods good = checkGoodsByDevice(deviceCode, deviceNumber);
        Map data = new HashMap();
        if (good == null) {
            data.put("info", "not found");
        } else {
            if (good.getStatus().equals(IN_STORAGE)){ //the goods is in warehouse
                List<Map> list = getUninstallGoodsListOfTechnician(collectedPeopleId);
                data.put("info", "ok");
                data.put("data", list);
                data.put("id", good.getId());
                data.put("deviceNumber", good.getNumber());
                data.put("deviceCode", good.getDeviceCode());
                data.put("deviceType", equipmentService.getEquipmentById(good.getEquipmentId()).getType());
            } else {
                data.put("info", "error");
            }
        }
        return data;
    }

    @Override
    public void outStoreGoods(int id, int operatorId) {
        Goods good = getGoodById(id);
        good.setOutStorageTime(new Date());
        good.setOutStoragePeopleId(operatorId);
        good.setStatus(OUT_STORAGE);
        good.setEditTime(new Date());
        good.setEditOperaterId(operatorId);
        goodsMapper.updateByPrimaryKeySelective(good);
    }

    @Override
    public List<Map> getTransferGoods(int id, int areaId1, int areaId2) {
        GoodsExample example = new GoodsExample();
        GoodsExample.Criteria criteria = example.createCriteria();
        criteria.andEquipmentIdEqualTo(id);
        criteria.andStorageAreaIdEqualTo(areaId1);
        criteria.andStatusEqualTo(IN_STORAGE);
        List<Goods> goods = goodsMapper.selectByExample(example);
        List<Map> data = new ArrayList<>();
        for (int i=0; i<goods.size(); i++) {
            Map map = new HashMap();
            Goods good = goods.get(i);
            map.put("id", good.getId());
            map.put("deviceCode", good.getDeviceCode());
            map.put("hardwareCode", good.getHardwareCode());
            data.add(map);
        }
        return data;
    }

    @Override
    public void transferGoods(List<Integer> ids, int areaId1, int areaId2, String operatorId) {
        for (int i=0; i<ids.size(); i++) {
            int id = ids.get(i);
            Goods good = getGoodById(id);
            editGoodsService.addRecord(good, Integer.parseInt(operatorId));
            good.setStorageAreaId(areaId2);
            good.setEditOperaterId(Integer.parseInt(operatorId));
            good.setEditTime(new Date());
            good.setInStorageTime(new Date());
            good.setInStoragePeopleId(Integer.parseInt(operatorId));
            goodsMapper.updateByPrimaryKeySelective(good);
        }
    }

    @Override
    public List<Map> getCheckGoodsInfo(int id, int areaId) {
        GoodsExample example = new GoodsExample();
        GoodsExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(IN_STORAGE);
        criteria.andStorageAreaIdEqualTo(areaId);
        criteria.andEquipmentIdEqualTo(id);
        List<Goods> goods = goodsMapper.selectByExample(example);
        List<Map> data = new ArrayList<>();
        for (int i=0; i<goods.size(); i++) {
            Goods good = goods.get(i);
            Map map = new HashMap();
            map.put("id", good.getId());
            map.put("modelNumber", equipmentService.getEquipmentById(good.getEquipmentId()).getModelNumber());
            map.put("deviceCode", good.getDeviceCode());
            map.put("hardwareCode", good.getHardwareCode());
            map.put("status", "库存异常");
            data.add(map);
        }
        return data;
    }

    @Override
    public void recordGoods(int id, String status) {
        CheckGoodsRecords records = new CheckGoodsRecords();
        records.setCheckTime(new Date());
        records.setGoodsId(id);
        records.setResult(status);
        Goods good = getGoodById(id);
        records.setAreaid(good.getStorageAreaId());
        checkGoodsRecordsMapper.insert(records);
    }

    @Override
    public List<Map> getGoodsRecords(Boolean unusual, Integer areaId) {
        List<Map> data = new ArrayList<>();
        CheckGoodsRecordsExample example = new CheckGoodsRecordsExample();
        CheckGoodsRecordsExample.Criteria criteria = example.createCriteria();
        criteria.andResultEqualTo(UNUSUAL);
        criteria.andAreaidEqualTo(areaId);
        List<CheckGoodsRecords> records = checkGoodsRecordsMapper.selectByExample(example);
        for (int i=0; i<records.size(); i++) {
            Map map = new HashMap();
            CheckGoodsRecords record = records.get(i);
            Goods good = getGoodById(record.getGoodsId());
            map.put("modelNumber", equipmentService.getEquipmentById(good.getEquipmentId()).getModelNumber());
            map.put("deviceCode", good.getDeviceCode());
            map.put("result", record.getResult());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            map.put("time", format.format(record.getCheckTime()));
            data.add(map);
        }
        if (!unusual) { // only show the normal records
            CheckGoodsRecordsExample example1 = new CheckGoodsRecordsExample();
            CheckGoodsRecordsExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andResultNotEqualTo(UNUSUAL);
            criteria1.andAreaidEqualTo(areaId);
            List<CheckGoodsRecords> records1 = checkGoodsRecordsMapper.selectByExample(example1);
            for (int i=0; i<records1.size(); i++) {
                Map map = new HashMap();
                CheckGoodsRecords record = records1.get(i);
                Goods good = getGoodById(record.getGoodsId());
                map.put("modelNumber", equipmentService.getEquipmentById(good.getEquipmentId()).getModelNumber());
                map.put("deviceCode", good.getDeviceCode());
                map.put("result", record.getResult());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                map.put("time", format.format(record.getCheckTime()));
                data.add(map);
            }
        }
        return data;
    }

    @Override
    public Map getGoodsInfoByDeviceNum(String deviceNumber) {
        GoodsExample example = new GoodsExample();
        GoodsExample.Criteria criteria = example.createCriteria();
        criteria.andNumberEqualTo(deviceNumber);
        List<Goods> goods = goodsMapper.selectByExample(example);
        if (goods.size() == 0) {
            return null;
        } else {
            Goods good = goods.get(0);
            Map data = new HashMap();
            data.put("id", good.getId());
            Equipment equipment = equipmentService.getEquipmentById(good.getEquipmentId());
            data.put("modelNumber", equipment.getModelNumber());
            data.put("manufacturer", equipment.getManufacturer());
            data.put("attribute", equipment.getAttribute());
            data.put("type", equipment.getType());
            data.put("status", good.getStatus());
            data.put("deviceNumber", good.getNumber());
            data.put("deviceCode", good.getDeviceCode());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            data.put("buyDate", format.format(good.getPurchaseTime()));
            if (good.getUsingCustomerId() != null) {
                data.put("customerName", customerService.getCustomerNameById(good.getUsingCustomerId()));
            } else {
                data.put("customerName", "");
            }
            data.put("inStorageTime", format.format(good.getInStorageTime()));
            data.put("outStorageTime", format.format(good.getOutStorageTime()));
            data.put("inStoragePeopleId", good.getInStoragePeopleId());
            data.put("inStoragePeople", employeeService.getEmployeeNameById(good.getInStoragePeopleId()));
            if (good.getOutStoragePeopleId()!= null) {
                data.put("outStoragePeopleId", good.getOutStoragePeopleId());
                data.put("outStoragePeople", employeeService.getEmployeeNameById(good.getOutStoragePeopleId()));
            }
            data.put("hardwareCode", good.getHardwareCode());
            data.put("areaId", good.getStorageAreaId());
            data.put("area", areaService.getAreaById(good.getStorageAreaId()));
            return data;
        }
    }

    @Override
    public void editGoods(int id, String modelNumber, String status, String deviceNumber, String buyDate, String inStorageTime, int inStoragePeopleId, String outStorageTime, int outStoragePeopleId, String hardwareCode, int areaId, String deviceCode, String operatorId) throws ParseException {
        EditGoodsBefore before = new EditGoodsBefore();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        before.setGoodsId(id);
        before.setHardwareCode(hardwareCode);
        before.setStorageAreaId(areaId);
        before.setEquipmentId(equipmentService.getEquipmentByModelNum(modelNumber).getId());
        before.setNumber(deviceNumber);
        before.setDeviceCode(deviceCode);
        before.setStatus(status);
        before.setInStoragePeopleId(inStoragePeopleId);
        before.setInStorageTime(format.parse(inStorageTime));
        before.setOutStoragePeopleId(outStoragePeopleId);
        before.setOutStorageTime(format.parse(outStorageTime));
        before.setPurchaseTime(format.parse(buyDate));
        before.setCreateTime(new Date());
        before.setOperatorId(Integer.parseInt(operatorId));
        before.setEditOperaterId(Integer.parseInt(operatorId));
        before.setEditTime(new Date());
        editGoodsBeforeMapper.insert(before);
    }

    @Override
    public Map checkChangesList(Integer currentPage, Integer pageSize) {
        EditGoodsBeforeExample editGoodsBeforeExample = new EditGoodsBeforeExample();
        EditGoodsBeforeExample.Criteria criteria = editGoodsBeforeExample.createCriteria();
        criteria.andIdIsNotNull();
        PageHelper.startPage(currentPage, pageSize);
        List<EditGoodsBefore> goodsBefore = editGoodsBeforeMapper.selectByExample(editGoodsBeforeExample);
        PageInfo<EditGoodsBefore> info =  new PageInfo<>(goodsBefore);
        List<Map> maps = new ArrayList<>();
        for (int i=0; i<info.getList().size(); i++) {
            EditGoodsBefore good = info.getList().get(i);
            Map map = getChangeGoodsInfo(good);
            maps.add(map);
        }
        Map data = new HashMap();
        data.put("list", maps);
        data.put("total", info.getTotal());
        return data;
    }

    private Map getChangeGoodsInfo(EditGoodsBefore good) {
        Map map = new HashMap();
        map.put("id", good.getId());
        map.put("modelNumber", equipmentService.getEquipmentById(good.getEquipmentId()).getModelNumber());
        map.put("status", good.getStatus());
        map.put("deviceNumber", good.getNumber());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        map.put("buyDate", format.format(good.getPurchaseTime()));
        if (good.getUsingCustomerId() != null) {
            map.put("customerName", customerService.getCustomerNameById(good.getUsingCustomerId()));
        }
        map.put("inStorageTime", format.format(good.getInStorageTime()));
        map.put("inStoragePeopleId", good.getInStoragePeopleId());
        map.put("inStoragePeopleName", employeeService.getEmployeeNameById(good.getInStoragePeopleId()));
        map.put("outStorageTime", format.format(good.getOutStorageTime()));
        map.put("outStoragePeopleId", good.getOutStoragePeopleId());
        map.put("outStoragePeopleName", employeeService.getEmployeeNameById(good.getOutStoragePeopleId()));
        map.put("hardwareCode", good.getHardwareCode());
        map.put("areaId", good.getStorageAreaId());
        map.put("areaName", areaService.getAreaById(good.getStorageAreaId()).getName());
        return map;
    }

    @Override
    public Map getSpecificGoodChanges(int id) {
        EditGoodsBefore before = editGoodsBeforeMapper.selectByPrimaryKey(id);
        Map changed = getChangeGoodsInfo(before);
        int goodId = before.getGoodsId();
        Goods good = goodsMapper.selectByPrimaryKey(goodId);
        Map old = getGoodsInfo(good);
        Map data = new HashMap();
        data.put("old", old);
        data.put("new", changed);
        return data;
    }

    @Override
    public void changeGoodSuggest(int id, Boolean result, int operatorId) {
        EditGoodsBefore before = editGoodsBeforeMapper.selectByPrimaryKey(id); //get the goods
        if (result) { //if the admin agrees to change
            Goods good = goodsMapper.selectByPrimaryKey(before.getGoodsId());
            editGoodsService.addRecord(good, operatorId);
            good.setInStoragePeopleId(before.getInStoragePeopleId());
            good.setInStorageTime(before.getInStorageTime());
            good.setEditTime(new Date());
            good.setEditOperaterId(operatorId);
            good.setOutStoragePeopleId(before.getOutStoragePeopleId());
            good.setStorageAreaId(before.getStorageAreaId());
            good.setNumber(before.getNumber());
            good.setStatus(before.getStatus());
            good.setOutStorageTime(before.getOutStorageTime());
            good.setUsingCustomerId(before.getUsingCustomerId());
            good.setDeviceCode(before.getDeviceCode());
            good.setHardwareCode(before.getHardwareCode());
            good.setPurchaseTime(before.getPurchaseTime());
            good.setEquipmentId(before.getEquipmentId());
            goodsMapper.updateByPrimaryKey(good);
        }
        editGoodsBeforeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Map findGoods(String modelNumber, String status, String deviceNumber, String buyDate, String customerName, String inStorageTime, Integer inStoragePeopleId, String outStorageTime, Integer outStoragePeopleId, String deviceCode, String hardwareCode, Integer areaId, Integer currentPage, Integer pageSize) throws ParseException {
        GoodsExample example = new GoodsExample();
        GoodsExample.Criteria criteria = example.createCriteria();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (modelNumber != null) {
            List<Integer> equipIds = equipmentService.getEquipmentsByNum(modelNumber);
            criteria.andEquipmentIdIn(equipIds);
        }
        if (status != null) {
            status = "%" + status + "%";
            criteria.andStatusLike(status);
        }
        if (deviceNumber != null) {
            deviceNumber = "%" + deviceNumber + "%";
            criteria.andNumberLike(deviceNumber);
        }
        if (buyDate != null) {
            criteria.andPurchaseTimeEqualTo(format.parse(buyDate));
        }
        if (customerName != null) {
            if(customerService.getCustomersByName(customerName) == null) {
                return null;
            } else {
                List<Integer>customerIds = customerService.getCustomerIdsByCustomers(customerService.getCustomersByName(customerName));
                criteria.andUsingCustomerIdIn(customerIds);
            }
        }
        if (inStorageTime != null) {
            criteria.andInStorageTimeEqualTo(format.parse(inStorageTime));
        }
        if (inStoragePeopleId != null) {
            criteria.andInStoragePeopleIdEqualTo(inStoragePeopleId);
        }
        if (outStorageTime != null) {
            criteria.andOutStorageTimeEqualTo(format.parse(outStorageTime));
        }
        if (outStoragePeopleId != null) {
            criteria.andOutStoragePeopleIdEqualTo(outStoragePeopleId);
        }
        if (deviceCode != null) {
            deviceCode = "%" + deviceCode + "%";
            criteria.andDeviceCodeLike(deviceCode);
        }
        if (hardwareCode != null) {
            hardwareCode = "%" + hardwareCode + "%";
            criteria.andHardwareCodeLike(hardwareCode);
        }
        if (areaId != null) {
            criteria.andStorageAreaIdEqualTo(areaId);
        }
        PageHelper.startPage(currentPage, pageSize);
        List<Goods> goods = goodsMapper.selectByExample(example);
        PageInfo<Goods> info = new PageInfo<>(goods);
        if (info.getTotal() == 0) {
            return null;
        } else {
            List<Map> list = new ArrayList<>();
            for (int i = 0; i < info.getList().size(); i++) {
                Goods good = info.getList().get(i);
                Map map = getGoodsInfo(good);
                list.add(map);
            }
            Map data = new HashMap();
            data.put("list", list);
            data.put("total", info.getTotal());
            return data;
        }

    }


    private List<Map> getUninstallGoodsListOfTechnician(Integer collectedPeopleId) {
        List<Map> data = new ArrayList<>();
        GoodsExample example = new GoodsExample();
        GoodsExample.Criteria criteria = example.createCriteria();
        criteria.andOutStoragePeopleIdEqualTo(collectedPeopleId);
        criteria.andStatusEqualTo(OUT_STORAGE);
        criteria.andUsingCustomerIdIsNull();
        List<Goods> goods = goodsMapper.selectByExample(example);
        for (int i=0; i<goods.size(); i++) {
            Goods good = goods.get(i);
            Map map = new HashMap();
            map.put("deviceNumber", good.getNumber());
            map.put("deviceCode", good.getDeviceCode());
            map.put("deviceType", equipmentService.getEquipmentById(good.getEquipmentId()).getType());
            data.add(map);
        }
        return data;
    }

    private Goods checkGoodsByDevice(String deviceCode, String deviceNumber) {
        GoodsExample example = new GoodsExample();
        GoodsExample.Criteria criteria = example.createCriteria();
        if (deviceCode != null) {
            criteria.andDeviceCodeEqualTo(deviceCode);
        }
        if (deviceNumber != null) {
            criteria.andNumberEqualTo(deviceNumber);
        }
        List<Goods> list = goodsMapper.selectByExample(example);
        if (list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    private Map getGoodsInfo(Goods good) {
        Map map = new HashMap();
        map.put("id", good.getId());
        map.put("equipmentId", good.getEquipmentId());
        Equipment equipment = equipmentService.getEquipmentById(good.getEquipmentId());
        map.put("modelNumber", equipment.getModelNumber());
        map.put("manufacturer", equipment.getManufacturer());
        map.put("attribute", equipment.getAttribute());
        map.put("type", equipment.getType());
        map.put("status", good.getStatus());
        map.put("deviceNumber", good.getNumber());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        map.put("buyDate", format.format(good.getPurchaseTime()));
        if (good.getUsingCustomerId()!= null) {
            map.put("customerName", customerService.getCustomerNameById(good.getUsingCustomerId()));
        }
        map.put("inStorageTime", format.format(good.getInStorageTime()));
        map.put("inStoragePeopleId", good.getInStoragePeopleId());
        map.put("inStoragePeopleName", employeeService.getEmployeeNameById(good.getInStoragePeopleId()));
        map.put("inStoragePeople", employeeService.getEmployeeNameById(good.getInStoragePeopleId()));
        if (good.getOutStoragePeopleId() != null) {
            map.put("outStorageTime", format.format(good.getOutStorageTime()));
            map.put("outStoragePeopleId", good.getOutStoragePeopleId());
        }
        map.put("outStoragePeopleName", employeeService.getEmployeeNameById(good.getOutStoragePeopleId()));
        map.put("outStoragePeople", employeeService.getEmployeeNameById(good.getOutStoragePeopleId()));
        map.put("hardwareCode", good.getHardwareCode());
        map.put("area", areaService.getAreaById(good.getStorageAreaId()).getName());
        map.put("areaId", good.getStorageAreaId());
        map.put("areaName", areaService.getAreaById(good.getStorageAreaId()).getName());
        map.put("deviceCodes", good.getDeviceCode());
        return map;
    }

    private Goods checkGoodsExistByDeviceCode(String deviceCode) {
        GoodsExample example = new GoodsExample();
        GoodsExample.Criteria criteria = example.createCriteria();
        criteria.andDeviceCodeEqualTo(deviceCode);
        List<Goods> goodsList = goodsMapper.selectByExample(example);
        if (goodsList.size() == 0) {
            return null;
        } else {
            return goodsList.get(0);
        }
    }

    @Override
    public int getTotalGoodsNumByEquipment(Equipment equipment) {
        GoodsExample example = new GoodsExample();
        GoodsExample.Criteria criteria = example.createCriteria();
        criteria.andEquipmentIdEqualTo(equipment.getId());
        return goodsMapper.selectByExample(example).size();
    }
}
