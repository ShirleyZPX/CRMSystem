package com.springboot.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.crm.dao.CustomerBusinessMapper;
import com.springboot.crm.dao.CustomerFollowUpRecordMapper;
import com.springboot.crm.dao.CustomerHistoryInfoMapper;
import com.springboot.crm.dao.CustomerMapper;
import com.springboot.crm.entity.*;
import com.springboot.crm.service.*;
import com.springboot.crm.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerHistoryInfoMapper customerHistoryInfoMapper;
    @Autowired
    CustomerFollowUpRecordMapper customerFollowUpRecordMapper;
    @Autowired
    CustomerBusinessMapper customerBusinessMapper;
    @Autowired
    AreaService areaService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    TypeService typeService;
    @Autowired
    CustomerBusinessService customerBusinessService;
    @Autowired
    BusinessService businessService;
    @Autowired
    BackUpService backUpService;

    private final static String STATUS_OFFICIAL = "正式客户";
    private final static String STATUS_CAN_DELETE = "可删除客户";
    private final static String STATUS_DELETE = "删除客户";

    private final static String DATA_CREATE = "新建";
    private final static String DATA_EDIT = "修改";
    private final static String DATA_DELETE = "删除";

    @Override
    public String getCustomerNameById(Integer id) {
        String name = customerMapper.selectByPrimaryKey(id).getName();
        return name;
    }

    @Override
    public List<Customer> getCustomersInAllAreas(List<Area> areas) {
        List<Integer> areaIds = areaService.getAreaIdsByAreas(areas);
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andAreaIdIn(areaIds);
        List<Customer> customers = customerMapper.selectByExample(example); //get all the customers in these areas
        return customers;
    }

    @Override
    public List<Customer> getCustomersByName(String customerName) { //名称模糊查询
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        customerName = "%" + customerName +"%";
        criteria.andNameLike(customerName); //模糊查询
        List<Customer> customers = customerMapper.selectByExample(example);

        return customers;
    }

    @Override
    public List<Customer> getCustomersByAreaId(Integer areaId) {
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andAreaIdEqualTo(areaId);
        List<Customer> customers = customerMapper.selectByExample(example);
        return customers;
    }

    public Customer getCustomerByName(String name){
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<Customer> customers = customerMapper.selectByExample(example);
        if (customers.size() == 0) {
            return null;
        } else {
            return customers.get(0);
        }
    }

    @Override
    public void downloadCustomerInfo(HttpServletResponse response, int id) throws IOException {
        List<List<String>> excelData = new ArrayList<>();

        List<String> head = new ArrayList<>();
        head.add("客户名称");
        head.add("客户曾用名");
        head.add("客户地址");
        head.add("客户归属地");
        head.add("客户联系人");
        head.add("客户电话1");
        head.add("客户电话2");
        head.add("客户经理");
        head.add("客户经理电话");
        head.add("业务经理");
        head.add("客户状态");
        head.add("填写时间");
        head.add("数据状态");
        head.add("操作人");
        excelData.add(head);
        Customer customer = getCustomerById(id);
        List<String> data = new ArrayList<>();
        data.add(customer.getName());
        data.add(customer.getPreviousName());
        data.add(customer.getAddress());
        data.add(customer.getArea());
        data.add(customer.getContactPerson());
        data.add(customer.getPhone1());
        data.add(customer.getPhone2());
        data.add(customer.getCustomerManager());
        data.add(customer.getManagerPhone());
        data.add(customer.getBusinessManager());
        data.add(customer.getStatus());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        data.add(format.format(customer.getCreateTime()));
        data.add(customer.getDataStatus());
        data.add(employeeService.getEmployeeNameById(customer.getOperatorId()));
        excelData.add(data);

        String date = format.format(new Date());
//        String fileName = "客户信息 " + date;
        String fileName = "客户信息+"+date;
        fileName = URLEncoder.encode(fileName,"UTF-8");
        Map map = getCustomerInfoWhenFind(id);
        Map[] businesses = (Map[]) map.get("businesses");
        if (businesses.length != 0) {
            List<String> businessHeader = new ArrayList<>();
            businessHeader.add("业务名称");
            businessHeader.add("套餐名称");
            businessHeader.add("办理日期");
            excelData.add(businessHeader);
            for(int i=0; i<businesses.length; i++) {
                Map businessData = businesses[i];
                List<String> dataBusiness = new ArrayList<>();
                dataBusiness.add((String) businessData.get("businessName"));
                dataBusiness.add((String) businessData.get("packageName"));
                dataBusiness.add((String) businessData.get("date"));
                excelData.add(dataBusiness);
            }
        }
        ExcelUtil.exportExcel(response, excelData, fileName, "Sheet1", 15);
    }

    @Override
    public void downloadAllCustomers(HttpServletResponse response) throws IOException {
        List<List<String>> excelData = new ArrayList<>();

        List<String> head = new ArrayList<>();
        head.add("客户名称");
        head.add("客户曾用名");
        head.add("客户地址");
        head.add("客户归属地");
        head.add("客户联系人");
        head.add("客户电话1");
        head.add("客户电话2");
        head.add("客户经理");
        head.add("客户经理电话");
        head.add("业务经理");
        head.add("客户状态");
        head.add("填写时间");
        head.add("数据状态");
        head.add("操作人");
        excelData.add(head);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Customer> customers = getAllCustomers();
        for (int i=0; i< customers.size(); i++) {
            Customer customer = customers.get(i);
            List<String> data = new ArrayList<>();
            data.add(customer.getName());
            data.add(customer.getPreviousName());
            data.add(customer.getAddress());
            data.add(customer.getArea());
            data.add(customer.getContactPerson());
            data.add(customer.getPhone1());
            data.add(customer.getPhone2());
            data.add(customer.getCustomerManager());
            data.add(customer.getManagerPhone());
            data.add(customer.getBusinessManager());
            data.add(customer.getStatus());
            data.add(format.format(customer.getCreateTime()));
            data.add(customer.getDataStatus());
            data.add(employeeService.getEmployeeNameById(customer.getOperatorId()));
            excelData.add(data);
        }
        String date = format.format(new Date());
//        String fileName = "客户信息 " + date;
        String fileName = "所有客户信息+"+date;
        fileName = URLEncoder.encode(fileName,"UTF-8");
        ExcelUtil.exportExcel(response, excelData, fileName, "Sheet1", 15);
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) throws UnsupportedEncodingException, FileNotFoundException {
        TemplateUtil templateUtil = new TemplateUtil();
        templateUtil.downloadTemplate(response, "customer");
    }

    @Override
    public Map uploadCustomerInfo(Customer customer) {
        Map data = new HashMap();
        Customer customer1 = getCustomerByName(customer.getName());
        if (customer1 == null) { //客户不存在
            Area area = areaService.getAreaByName(customer.getArea());
            if (area == null) {
                data.put("info", "Area not found");
            }
            Employee customerManager = employeeService.getEmployeeByName(customer.getCustomerManager());
            if (customerManager == null) {
                data.put("info", "Customer Manager not found");
            }
            Employee businessManager = employeeService.getEmployeeByName(customer.getBusinessManager());
            if (businessManager == null) {
                data.put("info", "Business Manager not found");
            }
            if (area != null && customerManager != null && businessManager != null) {
                addCustomer(customer);
                data.put("info", "add");
            }
        } else { //customer exist
            data.put("info", "exist");
            Map old = getCustomerInfo(customer1);
            data.put("old", old);
            Map newCus  = getCustomerInfo(customer);
            data.put("new", newCus);
        }
        return data;
    }

    @Override
    public void addFollowRecord(int id, String text, String date, String base64, String type, int operatorId) throws ParseException {
        CustomerFollowUpRecord record = new CustomerFollowUpRecord();
        record.setCustomerId(id);
        record.setDetail1(text);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        record.setDate(format.parse(date));
        FileTransferUtil fileTransferUtil = new FileTransferUtil();
        String fileName = getCustomerNameById(id)+"+"+date+type;
        String path = fileTransferUtil.base64ToFile(base64, fileName, type);
        record.setDetail2(path);
        customerFollowUpRecordMapper.insert(record);
        backUpService.addBackup(operatorId, new Date(), "add follow up record");
    }

    @Override
    public Map getCustomerInfoById(int id) {
        Customer customer = getCustomerById(id);
        return getCustomerInfo(customer);
    }

    @Override
    public void changeAreaNameByAreaId(Integer areaId, String name) {
        List<Customer> customers = getCustomersByAreaId(areaId);
        for (int i=0; i<customers.size(); i++) {
            Customer customer= customers.get(i);
            customer.setArea(name);
        }
    }

    @Override
    public List<Customer> getManagerByEmployee(Employee employee) {
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerManagerIdEqualTo(employee.getId());
        CustomerExample.Criteria criteria1 = example.createCriteria();
        criteria1.andBusinessManagerIdEqualTo(employee.getId());
        example.or(criteria1);
        return customerMapper.selectByExample(example);
    }
/*
@param id the is of the past manager
@param handOverId is the id of the manager will transfer the customer to
 */
    @Override
    public void changeManagerOFCustomers(List<Customer> customers, int id, int handOverId, int operatorId) {
        for (int i=0; i<customers.size(); i++) {
            Customer customer = customers.get(i);
            addEditRecord(customer, 0);
            if (customer.getCustomerManagerId() != null) {
                if (customer.getCustomerManagerId().equals(id)) {
                    customer.setCustomerManagerId(handOverId);
                    customer.setCustomerManager(employeeService.getEmployeeNameById(handOverId));
                }
            }
            if (customer.getBusinessManagerId() != null) {
                if (customer.getBusinessManagerId().equals(id)) {
                    customer.setBusinessManagerId(handOverId);
                    customer.setBusinessManager(employeeService.getEmployeeNameById(handOverId));
                }
            }
            customer.setEditTime(new Date());
            customer.setEditOperatorId(operatorId);
            customer.setDataStatus(DATA_EDIT);
            customerMapper.updateByPrimaryKeySelective(customer);
        }
    }

    @Override
    public Map[] getAllCustomersByNameLikeByPage(String name, int id, int currentPage, int pageSize) {
        return getCustomersByNameLikePage(name, id, currentPage, pageSize, false);
    }

    private List<Customer> getAllCustomers() {
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        return customerMapper.selectByExample(example);
    }

    @Override
    public boolean addCustomer(Customer customer) {
        Employee manager = employeeService.getEmployeeByName(customer.getCustomerManager());
        int typeId1 = typeService.getIdByName(RolesUtil.SALES_MANAGER); //get the role's id
        int typeId2 = typeService.getIdByName(RolesUtil.SALESMAN); //get the role's id

        if (manager == null) { //客户经理不存在
            return false;
        } else {
            if (employeeService.checkEmployeeTwoRole(manager, typeId1, typeId2)){
                int customerManagerId = manager.getId();
                customer.setCustomerManagerId(customerManagerId);
                if (customer.getBusinessManagerId() != null) {
                    customer.setBusinessManager(employeeService.getEmployeeNameById(customer.getBusinessManagerId()));
                }
                if (customer.getBusinessManager() != null) {
                    customer.setBusinessManagerId(employeeService.getEmployeeByName(customer.getBusinessManager()).getId());
                }
                if (customer.getAreaId() != null) {
                    customer.setArea(areaService.getAreaById(customer.getAreaId()).getName());
                }
                if (customer.getArea() != null) {
                    customer.setAreaId(areaService.getAreaByName(customer.getArea()).getId());
                }
                if(customer.getOperator() != null) {
                    customer.setOperatorId(employeeService.getEmployeeByName(customer.getOperator()).getId());
                }
                customer.setStatus(STATUS_OFFICIAL);
                customer.setDataStatus(DATA_CREATE);
                customer.setCreateTime(new Date());
                customer.setEditTime(new Date());
                customer.setEditOperatorId(customer.getOperatorId());
                customerMapper.insert(customer);
                backUpService.addBackup(customer.getOperatorId(), customer.getCreateTime(), "add customer");
                return true;
            }
            return false;
        }
    }

    @Override
    public Customer checkCustomerAdd(String name, int operatorId) {
        List<Area> areas = areaService.getAreasByOperatorId(operatorId);
        List<Integer> areaIds = areaService.getAreaIdsByAreas(areas);
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andAreaIdIn(areaIds);
        criteria.andNameEqualTo(name);
        List<Customer> customers = customerMapper.selectByExample(example);
        if (customers.size() > 0) {
            return customers.get(0);
        } else {
            return null;
        }

    }

    private Map[] getCustomersByNameLikePage(String name, int id, int currentPage, int pageSize, boolean status) {
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        if (name.equals("null")) {
            criteria.andNameIsNotNull();
        } else {
//        if(StringUtils.isNotBlank(name)){
            name = "%" + name + "%";
//        }
//        if(StringUtils.isNotBlank(name)){
            criteria.andNameLike(name);
        }
//        }
        if (status) { //只筛选出正式用户和可删除客户
            criteria.andStatusNotEqualTo(STATUS_DELETE); //choose the other two
        }

        List<Area> areas = areaService.getAreasByOperatorId(id); //get all the area the user manage
        List<Integer> areaIds = areaService.getAreaIdsByAreas(areas);
        criteria.andAreaIdIn(areaIds);
        PageHelper.startPage(currentPage, pageSize);
        List<Customer> customers = customerMapper.selectByExample(example);
        PageInfo<Customer> info = new PageInfo<>(customers);
        List<Customer> data = info.getList();
        Map[] map = new Map[data.size()];
        for (int i=0; i<data.size(); i++){
            Map dataMap = new HashMap();
            Customer customer = data.get(i);
            dataMap.put("id", customer.getId());
            dataMap.put("name", customer.getName());
            map[i] = dataMap;
        }
        return map;
    }

    @Override
    public Map[] getCustomersByNameLikeByPage(String name, int id, int currentPage, int pageSize) {
        return getCustomersByNameLikePage(name, id, currentPage, pageSize, true);
    }

    @Override
    public Map[] getBusinessManagerByArea(int areaId) {
        List<Employee>  managers = employeeService.getBusinessManagerByArea(areaId);
        Map[] data = new Map[managers.size()];
        for (int i=0; i<managers.size(); i++){
            Map map = new HashMap();
            Employee manager = managers.get(i);
            map.put("id", manager.getId());
            map.put("name", manager.getName());
            data[i] = map;
        }
        return data;
    }

    private void addEditRecord(Customer customer, int status){
        CustomerHistoryInfo customerHistoryInfo = new CustomerHistoryInfo();
        customerHistoryInfo.setAddress(customer.getAddress());
        customerHistoryInfo.setAreaId(customer.getAreaId());
        customerHistoryInfo.setBusinessManagerId(customer.getBusinessManagerId());;
        customerHistoryInfo.setContactPerson(customer.getContactPerson());
        customerHistoryInfo.setCreateTime(new Date());
        customerHistoryInfo.setCustomerId(customer.getId());
        customerHistoryInfo.setCustomerManagerId(customer.getCustomerManagerId());
        customerHistoryInfo.setDataStatus(customer.getDataStatus());
        customerHistoryInfo.setManagerPhone(customer.getManagerPhone());
        customerHistoryInfo.setName(customer.getName());
        customerHistoryInfo.setOperatorId(customer.getOperatorId());
        if (status == 0){ //修改信息
            customerHistoryInfo.setOperatorType(DATA_EDIT);
        } else{ //删除信息
            customerHistoryInfo.setOperatorType(DATA_DELETE);
        }
        customerHistoryInfo.setPhone1(customer.getPhone1());
        customerHistoryInfo.setPhone2(customer.getPhone2());
        customerHistoryInfo.setPreviousName(customer.getPreviousName());
        customerHistoryInfo.setStatus(customer.getStatus());
        customerHistoryInfoMapper.insert(customerHistoryInfo);
    }

    @Override
    public boolean editCustomer(int id, Customer customer) {
        int customerManagerId = -1;
        if (customer.getCustomerManager() != null) {
            Employee customerManager = employeeService.getEmployeeByName(customer.getCustomerManager());
            int typeId1 = typeService.getIdByName(RolesUtil.SALES_MANAGER); //get the role's id
            int typeId2 = typeService.getIdByName(RolesUtil.SALESMAN); //get the role's id
            if (customerManager == null) {
                return false;
            } else {
                if (employeeService.checkEmployeeTwoRole(customerManager, typeId1, typeId2)) {
                     customerManagerId = customerManager.getId();
                } else {
                    return false;
                }
            }
        }
        addEditRecord(getCustomerById(id), 0);
        if (customerManagerId != -1) {
            customer.setCustomerManagerId(customerManagerId);
        }
        if (customer.getAreaId() != null) {
            customer.setArea(areaService.getAreaById(customer.getAreaId()).getName());
        }
        customer.setId(id);
        customer.setStatus(STATUS_OFFICIAL);
        customer.setDataStatus(DATA_EDIT);
        if (customer.getBusinessManagerId() != null) {
            customer.setBusinessManager(employeeService.getEmployeeNameById(customer.getBusinessManagerId()));
        }
        customer.setEditOperatorId(customer.getOperatorId());
        customer.setEditTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
        backUpService.addBackup(customer.getOperatorId(), customer.getEditTime(), "edit customer");
        return true;
    }

    public Map getCustomerInfo(Customer customer){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Map map = new HashMap();
        map.put("id", customer.getId());
        map.put("name", customer.getName());
        map.put("previousName", customer.getPreviousName());
        map.put("address", customer.getAddress());
        map.put("area", customer.getArea());
        map.put("areaId", customer.getAreaId());
        map.put("contactPerson", customer.getContactPerson());
        map.put("phone1", customer.getPhone1());
        map.put("phone2", customer.getPhone2());
        map.put("customerManager", customer.getCustomerManager());
        map.put("managerPhone", customer.getManagerPhone());
        map.put("businessManagerId", customer.getBusinessManagerId());
        map.put("businessManager", customer.getBusinessManager());
        if (customer.getCreateTime() == null) {
            map.put("createTime", null);
        } else {
            map.put("createTime", format.format(customer.getCreateTime()));
        }
        map.put("status", customer.getStatus());
        map.put("operatorId", customer.getOperatorId());
        map.put("operator", employeeService.getEmployeeNameById(customer.getOperatorId()));
        return map;
    }

    @Override
    public Map checkCustomerDelete(int id) {
        Customer customer = customerMapper.selectByPrimaryKey(id);
        Map[] maps = customerBusinessService.getCustomerBusinessInfoByCustomer(customer);
        Map data = new HashMap();
        data.put("delete", true); //可以删除
        for (int i=0; i<maps.length; i++) {
            Map map = maps[i];
            String orderType = (String) map.get("orderType");
            if (orderType.equals("装机")) {
                CustomerBusiness customerBusiness = (CustomerBusiness) map.get("business");
                if (customerBusinessService.getUninstallBusiness(customerBusiness.getId()).size() == 0) { //在一个业务处并未关联一个拆机业务，代表机器并未拆除
                    data.put("delete", false); //不可以删除
                }
            }
        }
        data.put("customer", getCustomerInfo(customer));
        data.put("business", maps);
        return data;
    }

    @Override
    public void deleteCustomer(int id, int operatorId) {
        Customer customer = customerMapper.selectByPrimaryKey(id);
        addEditRecord(customer, 1);
        customer.setId(id);
        customer.setDataStatus(DATA_DELETE);
        customer.setStatus(STATUS_DELETE);
        customer.setEditOperatorId(operatorId);
        customer.setEditTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
        backUpService.addBackup(operatorId, customer.getEditTime(), "deleteCustomer");
    }

    public List<Integer> getCustomerIdsByCustomers(List<Customer> customers){
        List<Integer> customerIds = new ArrayList<>();
        for (int i=0; i<customers.size(); i++){
            Customer customer = customers.get(i);
            int id = customer.getId();
            customerIds.add(id);
        }
        return customerIds;
    }

    @Override
    public Map[] getUnclaimedList(int operatorId, int currentPage, int pageSize) {
        List<Area> areas = areaService.getAreasByOperatorId(operatorId);
        List<Customer> customers = getCustomersInAllAreas(areas); //get all customers in these areas
        List<Integer> customerIds = getCustomerIdsByCustomers(customers);
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(customerIds);
        criteria.andStatusNotEqualTo(STATUS_DELETE); //only want the customer that are available
        criteria.andBusinessManagerIdIsNull(); //don't have business manager
        PageHelper.startPage(currentPage, pageSize);
        List<Customer> unclaimedCustomers = customerMapper.selectByExample(example);
        PageInfo<Customer> info = new PageInfo<>(unclaimedCustomers);
        List<Customer> customerList = info.getList();
        Map[] maps = new Map[customerList.size()];
        for (int i=0; i<customerList.size(); i++){
            Customer customer = customerList.get(i);
            Map map = new HashMap();
            map.put("id", customer.getId());
            map.put("name", customer.getName());
            map.put("address", customer.getAddress());
            map.put("areaId", customer.getAreaId());
            map.put("area", areaService.getAreaById(customer.getAreaId()).getName());
            map.put("customerManager", employeeService.getEmployeeNameById(customer.getCustomerManagerId()));
            maps[i] = map;
        }
        return maps;
    }

    @Override
    public void claimCustomer(int id, int managerId, int operatorId) {
        Customer customer = customerMapper.selectByPrimaryKey(id);
        addEditRecord(customer, 0);
        customer.setBusinessManagerId(managerId);
        customer.setBusinessManager(employeeService.getEmployeeNameById(managerId));
        customer.setEditOperatorId(operatorId);
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    @Override
    public Map getCustomerFollowRecord(int id) {
        Customer customer = customerMapper.selectByPrimaryKey(id);
        Map data = new HashMap();
        Map infoMap = getCustomerInfo(customer);
        data.put("customerInfo", infoMap);
        CustomerFollowUpRecordExample example = new CustomerFollowUpRecordExample();
        CustomerFollowUpRecordExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(id);
        example.setOrderByClause("date asc");
        List<CustomerFollowUpRecord> records = customerFollowUpRecordMapper.selectByExample(example);//get all the records of the customer
        Map[] recordsMap = new Map[records.size()];
        for (int i=0; i<records.size(); i++){
            CustomerFollowUpRecord record = records.get(i);
            Map map = new HashMap();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            map.put("date", format.format(record.getDate()));
            map.put("followText", record.getDetail1());
            FileTransferUtil fileTransferUtil = new FileTransferUtil();
            String path = record.getDetail2();
            String base64 = fileTransferUtil.fileToBase64(path);
            map.put("followFile", base64);
            System.out.println(base64);
            String info[] = path.split("/");
            int size = info.length;
            map.put("fileName", info[size-1]);
            recordsMap[i] = map;
        }
        data.put("records", recordsMap);
        return data;
    }

    @Override
    public Map[] findCustomer(String name, String previousName, String address, Integer areaId, String contactPerson, String phone1, String phone2, String customerManager, String managerPhone, Integer businessManagerId, Integer currentPage, Integer pageSize) {
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        if (name != null){
            name = "%" + name + "%";
            criteria.andNameLike(name);
        }
        if (previousName != null){
            previousName = "%" + previousName + "%";
            criteria.andPreviousNameLike(previousName);
        }
        if (address != null){
            address = "%" + address + "%";
            criteria.andAddressLike(address);
        }
        if (areaId != null){
            criteria.andAreaIdEqualTo(areaId);
        }
        if (contactPerson != null){
            contactPerson = "%" + contactPerson + "%";
            criteria.andContactPersonLike(contactPerson);
        }
        if (phone1 != null){
            phone1 = "%" + phone1 + "%";
            criteria.andPhone1Like(phone1);
        }
        if (phone2 != null){
            phone2 = "%" + phone2 + "%";
            criteria.andPhone2Like(phone2);
        }
        if (customerManager != null){
            customerManager = "%" + customerManager + "%";
            criteria.andCustomerManagerLike(customerManager);
        }
        if (managerPhone != null){
            managerPhone = "%" + managerPhone + "%";
            criteria.andManagerPhoneLike(managerPhone);
        }
        if (businessManagerId != null){
            criteria.andBusinessManagerIdEqualTo(businessManagerId);
        }
        PageHelper.startPage(currentPage, pageSize);
        List<Customer> customers = customerMapper.selectByExample(example);
        PageInfo<Customer> info = new PageInfo<>(customers);
        List<Customer> dataCustomers = info.getList();
        Map[] data = new Map[dataCustomers.size()];
        for (int i=0; i<dataCustomers.size(); i++){
            Map map = new HashMap();
            Customer customer = dataCustomers.get(i);
            map.put("id", customer.getId());
            map.put("name", customer.getName());
            data[i] = map;
        }
        return data;
    }

    @Override
    public Map getCustomerInfoWhenFind(int id) {
        Customer customer = customerMapper.selectByPrimaryKey(id);
        Map map = new HashMap();
        Map customerInfo = getCustomerInfo(customer);
        map.put("customerInfo", customerInfo);
        Map[] businesses = customerBusinessService.getCustomerBusinessInfoByCustomer(customer);
        map.put("businesses", businesses);
        return map;
    }

    @Override
    public Map[] getAllUndeleteCustomers() {
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(STATUS_DELETE);
        List<Customer> customers = customerMapper.selectByExample(example);
        Map[] maps = new Map[customers.size()];
        for (int i=0; i<customers.size(); i++){
            Customer customer = customers.get(i);
            Map map = new HashMap();
            map.put("id", customer.getId());
            map.put("name", customer.getName());
            maps[i] = map;
        }

        return maps;
    }

    private List<Customer> getNewCustomers(List<Integer> customerIds){
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(customerIds);
        Date now = new Date();
        DateUtil dateUtil = new DateUtil();
        criteria.andCreateTimeBetween(dateUtil.getMinMonthDate(now), dateUtil.getMaxMonthDate(now));
        criteria.andStatusEqualTo(STATUS_OFFICIAL);
        return customerMapper.selectByExample(example);
    }

    private List<Customer> getDeleteCustomers(List<Integer> customerIds){
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(customerIds);
        Date now = new Date();
        DateUtil dateUtil = new DateUtil();
        criteria.andEditTimeBetween(dateUtil.getMinMonthDate(now), dateUtil.getMaxMonthDate(now));
        criteria.andStatusEqualTo(STATUS_DELETE);
        return customerMapper.selectByExample(example);
    }

    private List<Customer> getAllOfficialCustomers(List<Integer> customerIds){
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(customerIds);
        criteria.andStatusEqualTo(STATUS_OFFICIAL);
        return customerMapper.selectByExample(example);
    }

    private long getSpecialCustomersByBusiness(int businessId, List<Customer> customers){
        if (customers.size() == 0){
            return 0;
        } else {
            CustomerBusinessExample example = new CustomerBusinessExample();
            CustomerBusinessExample.Criteria criteria = example.createCriteria();
            criteria.andBusinessIdEqualTo(businessId);
            criteria.andCustomerIdIn(getCustomerIdsByCustomers(customers));
            return customerBusinessMapper.countByExample(example);
        }
    }

    @Override
    public Map[] getMainPage(int operatorId) {
        List<Area> areas = areaService.getAreasByOperatorId(operatorId);
        List<Customer> customers = getCustomersInAllAreas(areas);
        List<Integer> customerIds = getCustomerIdsByCustomers(customers);
        List<Customer> newCustomers = getNewCustomers(customerIds);
        List<Customer> deleteCustomers = getDeleteCustomers(customerIds);
        List<Customer> officialCustomers = getAllOfficialCustomers(customerIds);
        List<Business> businesses = businessService.getAllBusinesses();
        Map[] data = new Map[businesses.size()];
        for (int i=0; i<businesses.size(); i++){
            Business business = businesses.get(i);
            Map map = new HashMap();
            map.put("businessName", business.getName());
            int businessId = business.getId();
            map.put("total", getSpecialCustomersByBusiness(businessId, officialCustomers));
            map.put("new", getSpecialCustomersByBusiness(businessId, newCustomers));
            map.put("delete", getSpecialCustomersByBusiness(businessId, deleteCustomers));
            data[i] = map;
        }
        return data;
    }

    @Override
    public Customer getCustomerById(int customerId) {
        Customer customer = customerMapper.selectByPrimaryKey(customerId);
        return customer;
    }

    @Override
    public List<Customer> getCustomersByNameAndAreaOfficial(int id, String customerName) {
        List<Area> areas = areaService.getAreasByOperatorId(id);
        List<Customer> customers = getCustomersInAllAreas(areas);
        List<Integer> customerIds = getCustomerIdsByCustomers(customers);
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(STATUS_OFFICIAL);
        customerName = "%" + customerName + "%";
        criteria.andNameLike(customerName);
        criteria.andIdIn(customerIds);
        return customerMapper.selectByExample(example);
    }

}
