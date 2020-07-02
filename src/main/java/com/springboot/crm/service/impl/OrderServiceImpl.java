package com.springboot.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.crm.dao.*;
import com.springboot.crm.entity.*;
import com.springboot.crm.request.Order;
import com.springboot.crm.request.Schedule;
import com.springboot.crm.request.UserList;
import com.springboot.crm.service.*;
import com.springboot.crm.util.OrderStatus;
import com.springboot.crm.util.RolesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    EditOrdersMapper editOrdersMapper;
    @Autowired
    AreaService areaService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerBusinessService customerBusinessService;
    @Autowired
    CustomerBusinessHistoryService customerBusinessHistoryService;
    @Autowired
    BusinessService businessService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    HardwareService hardwareService;
    @Autowired
    SoftwareService softwareService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    EditOrdersService editOrdersService;
    @Autowired
    OrderHardwareMapper hardwareMapper;
    @Autowired
    OrderSoftwareMapper softwareMapper;
    @Autowired
    OrderPermissionMapper permissionMapper;
    @Autowired
    GoodsMapper goodsMapper;

    private List<Customer> getCustomerListInUserManagerArea(Integer id) {
        List<Area> areas = areaService.getAreasByOperatorId(id); //get areas the user manage
        List<Customer> customers = customerService.getCustomersInAllAreas(areas);
        return customers;
    }

    private int checkUserRoles(Integer id) { //check if the user is a business manager
        Set<Type> roles = employeeService.getRolesById(id); //get the roles of the user
        Iterator<Type> iterator = roles.iterator();
        while(iterator.hasNext()) {
            String roleName = iterator.next().getName();
            if(roleName == RolesUtil.SALES_MANAGER || roleName == RolesUtil.SALESMAN) {
                return 0; //业务经理
            } else if(roleName == RolesUtil.TECH) {
                return 1; //技术人员
            }
        }
        return 2; //支撑人员
    }

    private int getOrderIdByBusinessId(int businessId) {
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessCustomerIdEqualTo(businessId);
        int orderId = ordersMapper.selectByExample(example).get(0).getId();
        return orderId;
    }


    public Map getOrderList(Integer id, Integer currentPage, Integer pageSize, int status) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        List<Map> orders = new ArrayList<>();
        //get the map from customer business
        Map<String, Object> dataMap = new HashMap<>();
        int role = checkUserRoles(id);
        if(role == 0) { //业务经理
            dataMap = customerBusinessService.getBusinessListOfManagerByPage(id, status, currentPage, pageSize);
        } else if(role == 1) { //技术人员
            dataMap = customerBusinessService.getBusinessListOfTechByPage(id, status, currentPage, pageSize);
        } else { //支撑人员
            dataMap = customerBusinessService.getBusinessListOfCustomersByPage(getCustomerListInUserManagerArea(id), status, currentPage, pageSize);
        }
        List<CustomerBusiness> businesses = (List<CustomerBusiness>) dataMap.get("list");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (int m=0; m<businesses.size();m++) {
            CustomerBusiness business = businesses.get(m); //get the customer business
            Map order = new HashMap();
            if(status == 0) {
                order.put("id", business.getId()); //客户业务id
            } else {
                order.put("id", getOrderIdByBusinessId(business.getId())); //工单id
            }
            order.put("customerId", business.getCustomerId());
            order.put("customerName", customerService.getCustomerNameById(business.getCustomerId()));
            order.put("businessId", business.getBusinessId());
            order.put("businessName", businessService.getBusinessNameById(business.getBusinessId()));
            order.put("businessNumber", business.getBusinessNumber());
            order.put("bandWidth", business.getBandWidth());
            order.put("businessManager", employeeService.getEmployeeNameById(business.getBusinessManagerId()));
            order.put("orderAcceptTime", format.format(business.getOrderAcceptTime()));
            orders.add(order); //add the order to the list
        }
        map.put("list", orders);
        map.put("total", dataMap.get("total"));
        return map;
    }

    @Override
    public Map getOrderInfoById(Integer id, Integer status) {
        Map<String, Object> map = new HashMap<>();
        if(status == 0) {//派单查询， id为客户业务
            map.put("businessInfo", getCustomerBusinessInfoById(id));
            CustomerBusiness customerBusiness = customerBusinessService.getCustomerBusinessById(id);
            String orderStatus = customerBusiness.getStatus();
            if (orderStatus.equals("拆机业务") || orderStatus.equals("试用结束业务")){
                CustomerBusiness officialBusiness = customerBusinessService.getCustomerBusinessById(customerBusiness.getOfficialId()); //get the official business of this uninstall business
                Map installedInfo = getInstalledDeviceByCustomerBusinessId(officialBusiness.getId());
                map.put("installDeviceInfo", installedInfo);
            }
        } else { //收单查询， id为工单
            map.put("businessInfo", getCustomerBusinessInfoById(ordersMapper.selectByPrimaryKey(id).getBusinessCustomerId()));
        }
        return map;
    }

    private Map getCustomerBusinessInfoById(Integer id) {
        CustomerBusiness customerBusiness = customerBusinessService.getCustomerBusinessById(id);
        Map data = new HashMap();
        data.put("customerId", customerBusiness.getCustomerId());
        data.put("customerName", customerService.getCustomerNameById(customerBusiness.getCustomerId()));
        data.put("businessId", customerBusiness.getBusinessId());
        data.put("businessName", businessService.getBusinessNameById(customerBusiness.getBusinessId()));
        data.put("status", customerBusiness.getStatus());
        data.put("installAddress", customerBusiness.getInstallAddress());
        data.put("orderNumber", customerBusiness.getOrderNumber());
        String orderType = customerBusiness.getOrderType();
        data.put("orderType", orderType);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        data.put("orderAcceptTime", format.format(customerBusiness.getOrderAcceptTime()));
        if (customerBusiness.getOrderReturnTime() != null) {
            data.put("orderReturnTime", format.format(customerBusiness.getOrderReturnTime()));
        }
        data.put("loadMode", customerBusiness.getLoadMode());
        data.put("businessNumber", customerBusiness.getBusinessNumber());
        data.put("bandWidth", customerBusiness.getBandWidth());
        data.put("unicomCustomerId", customerBusiness.getUnicomCustomerId());
        String transactType = customerBusiness.getTransactType();
        data.put("transactType", transactType);
        data.put("packageId", customerBusiness.getPackageNameId());
        data.put("packageName", businessService.getPackageNamebyId(customerBusiness.getPackageNameId()));
        data.put("price", businessService.getPriceByPackageId(customerBusiness.getPackageNameId()));
        if (customerBusiness.getCustomerManager() != null) {
            data.put("customerManager", customerBusiness.getCustomerManager());
        }
        data.put("managerPhone", customerBusiness.getManagerPhone());
        if (customerBusiness.getBusinessManagerId() != null) {
            data.put("businessManagerId", customerBusiness.getBusinessManagerId());
            data.put("businessManagerName", employeeService.getEmployeeNameById(customerBusiness.getBusinessManagerId()));
        }
        data.put("orderStatus", customerBusiness.getOrderStatus());
        data.put("operator", employeeService.getEmployeeNameById(customerBusiness.getOperatorId()));
        data.put("dataStatus", customerBusiness.getDataStatus());
        return data;
    }

    @Override
    public List<UserList> getAllTechnicists(int id) {
        List<UserList> technicists = new ArrayList<>();
        List<Area> areas = areaService.getAreasByOperatorId(id); //get areas the user manage
        List<Employee> employees = employeeService.getAllTechnicists(areas); //get all the technicists
        for (int i=0; i<employees.size(); i++) {
            Employee employee = employees.get(i);
            UserList user = new UserList();
            user.setId(employee.getId());
            user.setName(employee.getName());
            technicists.add(user);
        }
        return technicists;
    }

    @Override
    public List<Map> getScheduleOfUser(int id) {
        List<Map> schedules = new ArrayList<>();
        List<Orders> orders = getOrdersHoldByTech(id);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for(int i=0; i<orders.size(); i++){
            Orders order = orders.get(i);
            String[] names = customerBusinessService.getCustomerBusinessNameById(order.getBusinessCustomerId());
            Map schedule = new HashMap();
            schedule.put("customerName", names[0]);
            schedule.put("businessName", names[1]);
            schedule.put("installTime", format.format(order.getInstallTime()));
            schedules.add(schedule);
        }
        return schedules;
    }

    public List<Orders> getOrdersHoldByTech(int id) {
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        criteria.andAcceptOrderPeopleIdEqualTo(id);
        example.setOrderByClause("install_time asc");//按照预约安装时间从前到后
        List<Orders> orders = ordersMapper.selectByExample(example); //get all the orders the techicist hold
        return orders;
    }

    @Override
    public void collectOrder(int orderId, int operatorId) {
        Orders order = ordersMapper.selectByPrimaryKey(orderId);
        int businessId = order.getBusinessCustomerId();
        CustomerBusiness business = new CustomerBusiness();
        business.setOrderStatus(OrderStatus.COLLECT); //change the order status to collect
        business.setEditOperatorId(operatorId);
        customerBusinessService.editCustomerBusiness(businessId, business); //update the data
    }

    @Override
    public void reassignedOrder(int id, int operatorId, int techId, String installTime) throws ParseException {
        Orders order = ordersMapper.selectByPrimaryKey(id);
        editOrdersService.addRecord(order, operatorId);
        order.setAcceptOrderPeopleId(techId);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date installDate = format.parse(installTime);
        order.setInstallTime(installDate);
        order.setOperatorId(operatorId);
        Date now = new Date();
        order.setEditTime(now);
        ordersMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public boolean returnOrder(int id, Map mapper) {
        Orders order = ordersMapper.selectByPrimaryKey(id);
        int operatorId = Integer.parseInt((String) mapper.get("operatorId"));
        editOrdersService.addRecord(order, operatorId);
        List<Map> installedDevices = (List<Map>) mapper.get("receiptInfo");
        for (int i=0; i<installedDevices.size(); i++) {
            Map map = installedDevices.get(i);
            Map hardware = (Map) map.get("reveive_hardware");
            if (hardware != null) {
                if(!addHardwareToOrder(hardware, order.getId())) {
                    return false;
                }
            }
            Map software = (Map) map.get("reveice_software");
            if (software!= null) {
                if( !addSoftwareToOrder(software, order.getId())) {
                    return false;
                }
            }
            Map permission = (Map) map.get("receive_license");
            if (permission!= null) {
                if( !addPermissionToOrder(permission, order.getId())) {
                    return false;
                }
            }
        }
        int businessId = order.getBusinessCustomerId();
        CustomerBusiness business = new CustomerBusiness();
        business.setOrderStatus(OrderStatus.RETURN); //set the order status to return
        business.setEditOperatorId(operatorId);
        customerBusinessService.editCustomerBusiness(businessId, business);
        return true;
    }

    private boolean addPermissionToOrder(Map map, int orderId) {
        Goods good = goodsService.getGoodsByGoodsCode((String) map.get("goodCode"));
        if (good == null) {
            return false;
        } else {
            if (map.get("equipmentStatus").equals("0")) {
                good.setEquipmentstatus("正式使用");
            } else {
                good.setEquipmentstatus("备用");
            }
            good.setBusinesslevel((String) map.get("businessLevel"));
            goodsMapper.updateByPrimaryKeySelective(good);
            OrderPermission permission = new OrderPermission();
            permission.setGoodCode((String) map.get("goodCode"));
            permission.setHardwareCode((String) map.get("hardwareCode"));
            permission.setGoodsId(good.getId());
            permission.setOrderId(orderId);
            permissionMapper.insert(permission);
            return true;
        }
    }

    private boolean addSoftwareToOrder(Map map, int orderId) {
        Goods good = goodsService.getGoodsByGoodsCode((String) map.get("goodCode"));
        if (good == null) {
            return false;
        } else {
            if (map.get("equipmentStatus").equals("0")) {
                good.setEquipmentstatus("正式使用");
            } else {
                good.setEquipmentstatus("备用");
            }
            good.setBusinesslevel((String) map.get("businessLevel"));
            goodsMapper.updateByPrimaryKeySelective(good);
            OrderSoftware software = new OrderSoftware();
            software.setGoodCode((String) map.get("goodCode"));
            software.setSoftwareExpireDate((String)map.get("softwareExpireDate"));
            software.setHardwareCode((String) map.get("hardwareCode"));
            software.setGoodsId(good.getId());
            software.setOrderId(orderId);
            softwareMapper.insert(software);
            return true;
        }
    }

    private boolean addHardwareToOrder(Map map, int orderId)  {
        Goods good = goodsService.getGoodsByGoodsCode((String) map.get("goodsCode"));
        if (good == null) {
            return false;
        } else {
            if (map.get("equipmentStatus").equals("0")) {
                good.setEquipmentstatus("正式使用");
            } else {
                good.setEquipmentstatus("备用");
            }
            good.setBusinesslevel((String) map.get("businessLevel"));
            goodsMapper.updateByPrimaryKeySelective(good);
            OrderHardware hardware = new OrderHardware();
            hardware.setGoodsId(good.getId());
            hardware.setGoodCode((String) map.get("goodsCode"));
            hardware.setDeployMode((String)map.get("deployMode"));
            hardware.setUpPortCode((String) map.get("upPortCode"));
            hardware.setDownPortCode((String) map.get("downPortCode"));
            hardware.setPublicIpv4((String) map.get("publicIpv4"));
            hardware.setPublicIpv6((String) map.get("publicIpv6"));
            hardware.setPrivateIpv4((String) map.get("privateIpv4"));
            hardware.setPrivateIpv6((String) map.get("privateIpv6"));
            hardware.setManageIpv4((String) map.get("manageIpv4"));
            hardware.setManageIpv6((String) map.get("manageIpv6"));
            hardware.setPlatformAddress((String) map.get("platformAddress"));
            hardware.setPlatformConnectMode((String) map.get("platformConnetMode"));
            hardware.setIpsUpdateTime((String) map.get("ipsUpdateTime"));
            hardware.setVirusDate((String) map.get("virusDate"));
            hardware.setAppRecognizeDate((String) map.get("appRecognizeDate")) ;
            hardware.setUrlDate((String) map.get("urlDate"));
            hardware.setWafDate((String) map.get("wafDate"));
            hardware.setOrderId(orderId);
            hardwareMapper.insert(hardware);
            return true;
        }
    }

    @Override
    public Orders getOrderById(int orderId) {
        Orders order = ordersMapper.selectByPrimaryKey(orderId);
        return order;
    }


    @Override
    public void withdrawOrder(int id, String withdrawReason, int operatorId) {
        Orders order = ordersMapper.selectByPrimaryKey(id);
        System.out.println(order.getId());
        editOrdersService.addRecord(order, operatorId);
        order.setReturnedReason(withdrawReason);
        System.out.println(order.getId());
        System.out.println(order.getReturnedReason());
        Date now = new Date();
        order.setEditTime(now);
        order.setOperatorId(operatorId);
        ordersMapper.updateByPrimaryKey(order);
        int businessId = order.getBusinessCustomerId();
        CustomerBusiness business = new CustomerBusiness();
        business.setOrderStatus(OrderStatus.WITHDRAW); //set the order status to return
        business.setEditOperatorId(operatorId);
        customerBusinessService.editCustomerBusiness(businessId, business);
    }

    private List<Integer> getCustomerBusinessIdByCustomerBusiness(List<CustomerBusiness> businesses) {
        List<Integer> businessIds = new ArrayList<>();
        if (businesses.size() != 0) {
            for (int i=0; i<businesses.size(); i++) {
                int id = businesses.get(i).getId();
                businessIds.add(id);
            }
        }

        return businessIds;
    }

    private OrdersExample.Criteria returnCriteriaByCondition(String customerName, int businessId, String businessNumber, Integer businessManagerId, String orderNumber, Integer technicistId, Integer areaId, String orderStatus, String orderStartTime, String orderEndTime, Integer operatorId) throws ParseException {
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();

        return criteria;
    }

    @Override
    public Map findOrderByPage(String customerName, String businessId, String businessNumber, Integer businessManagerId, String orderNumber, Integer technicistId, Integer areaId, String orderStatus, String orderStartTime, String orderEndTime, Integer currentPage, Integer pageSize, Integer operatorId) throws ParseException {
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        if (customerName != null){
            List<Customer> customers = customerService.getCustomersByName(customerName); //客户名称模糊查询
            List<Integer> customerIds = getCustomerIdsByCustomers(customers);
            if (customerIds.size() != 0) {
                List<CustomerBusiness> businesses = customerBusinessService.getBusinessByCustomerIds(customerIds);
                List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
                criteria.andBusinessCustomerIdIn(businessIds);
            } else {
                return null; //查找信息不存在
            }
        }
        if (businessId != null) {
            List<CustomerBusiness> businesses = customerBusinessService.getBusinessesByBusinessId(Integer.parseInt(businessId)); //get all the customer businesses by business id
            List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
            if (businessIds.size() != 0) { //find the customer business by businessId
                criteria.andBusinessCustomerIdIn(businessIds);
            } else {
                return null;
            }

        }
        if (businessNumber != null) {
            List<CustomerBusiness> businesses = customerBusinessService.getBusinessesByBusinessNumber(businessNumber); //get all the customer businesses by business number
            List<Integer> businessNumbers = getCustomerBusinessIdByCustomerBusiness(businesses);
            if (businessNumbers.size() != 0) { //find the customer business by businessNumber
                criteria.andBusinessCustomerIdIn(businessNumbers);
            } else {
                return null;
            }
        }
        if (businessManagerId != null) {
            List<CustomerBusiness> businesses = customerBusinessService.getBusinessesByBusinessManagerId(businessManagerId); //get all the customer businesses by business manager id
            List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
            if (businessIds.size() != 0) { //find the customer business by business manager id
                criteria.andBusinessCustomerIdIn(businessIds);
            } else {
                return null;
            }
        }
        if (orderNumber != null) {
            List<CustomerBusiness> businesses = customerBusinessService.getBusinessesByOrderNumber(orderNumber); //get all the customer businesses by order number
            List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
            if (businessIds.size() != 0) { //find the customer business by order number
                criteria.andBusinessCustomerIdIn(businessIds);
            } else {
                return null;
            }
        }
        if (technicistId != null) {
            criteria.andAcceptOrderPeopleIdEqualTo(technicistId);
        }
        if (areaId != null) {
            List<Customer> customers = customerService.getCustomersByAreaId(areaId); //get all the customers in this area
            List<Integer> customerIds = getCustomerIdsByCustomers(customers);
            if (customerIds.size() != 0){
                List<CustomerBusiness> businesses = customerBusinessService.getBusinessByCustomerIds(customerIds);
                List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
                criteria.andBusinessCustomerIdIn(businessIds);
            } else {
                return null; //查找信息不存在
            }
        }
        if (orderStatus != null){
            List<CustomerBusiness> businesses = customerBusinessService.getBusinessesByOrderStatus(orderStatus); //get all the customer businesses by order status
            List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
            if (businessIds.size() != 0) { //find the customer business by order status
                criteria.andBusinessCustomerIdIn(businessIds);
            } else {
                return null;
            }
        }
        if (orderStartTime != null && orderEndTime != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Date start = format.parse(orderStartTime);
            Date end = format.parse(orderEndTime);
            criteria.andInstallTimeBetween(start, end);
        }
        //登录账号负责区域工单
        List<Orders> orders = getOrdersOfLoginUser(operatorId);
        List<Integer> orderIds = getOrderIdsByOrders(orders);
        criteria.andIdIn(orderIds);
        PageHelper.startPage(currentPage, pageSize);
        List<Orders> ordersReturn = ordersMapper.selectByExample(example);
        PageInfo<Orders> info = new PageInfo<>(ordersReturn);
        Map<String, Object> map = new HashMap<>();
        List<Map> list = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (int i=0; i<info.getList().size(); i++){
            Map data = new HashMap();
            Orders order = info.getList().get(i);
            data.put("id", order.getId());
            CustomerBusiness customerBusiness = customerBusinessService.getCustomerBusinessById(order.getBusinessCustomerId());
            data.put("customerName", customerService.getCustomerNameById(customerBusiness.getCustomerId()));
            data.put("customerId", customerBusiness.getCustomerId());
            data.put("businessId", customerBusiness.getBusinessId());
            data.put("businessName", businessService.getBusinessNameById(customerBusiness.getBusinessId()));
            data.put("businessNumber", customerBusiness.getBusinessNumber());
            data.put("businessManagerId", customerBusiness.getBusinessManagerId());
            data.put("businessManager", employeeService.getEmployeeNameById(customerBusiness.getBusinessManagerId()));
            data.put("orderNumber", customerBusiness.getOrderNumber());
            data.put("technicistId", order.getAcceptOrderPeopleId());
            data.put("technicist", employeeService.getEmployeeNameById(order.getAcceptOrderPeopleId()));
            data.put("area", areaService.getAreaById(customerService.getCustomerById(customerBusiness.getCustomerId()).getAreaId()).getName());
            data.put("orderStatus", customerBusiness.getOrderStatus());
            data.put("orderAcceptTime", format.format(customerBusiness.getOrderAcceptTime()));
            list.add(data);
        }
        map.put("list", list);
        map.put("total", info.getTotal());
        return map;
    }

    @Override
    public Map getNumberOfEachStatusInCondition(String customerName, String businessId, String businessNumber, Integer businessManagerId, String orderNumber, Integer technicistId, Integer areaId, String orderStatus, String orderStartTime, String orderEndTime, Integer operatorId) throws ParseException {
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        if (customerName != null){
            List<Customer> customers = customerService.getCustomersByName(customerName); //客户名称模糊查询
            List<Integer> customerIds = getCustomerIdsByCustomers(customers);
            if (customerIds.size() != 0) {
                List<CustomerBusiness> businesses = customerBusinessService.getBusinessByCustomerIds(customerIds);
                List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
                criteria.andBusinessCustomerIdIn(businessIds);
            } else {
                return null; //查找信息不存在
            }
        }
        if (businessId != null) {
            List<CustomerBusiness> businesses = customerBusinessService.getBusinessesByBusinessId(Integer.parseInt(businessId)); //get all the customer businesses by business id
            List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
            if (businessIds.size() != 0) { //find the customer business by businessId
                criteria.andBusinessCustomerIdIn(businessIds);
            } else {
                return null;
            }

        }
        if (businessNumber != null) {
            List<CustomerBusiness> businesses = customerBusinessService.getBusinessesByBusinessNumber(businessNumber); //get all the customer businesses by business number
            List<Integer> businessNumbers = getCustomerBusinessIdByCustomerBusiness(businesses);
            if (businessNumbers.size() != 0) { //find the customer business by businessNumber
                criteria.andBusinessCustomerIdIn(businessNumbers);
            } else {
                return null;
            }
        }
        if (businessManagerId != null) {
            List<CustomerBusiness> businesses = customerBusinessService.getBusinessesByBusinessManagerId(businessManagerId); //get all the customer businesses by business manager id
            List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
            if (businessIds.size() != 0) { //find the customer business by business manager id
                criteria.andBusinessCustomerIdIn(businessIds);
            } else {
                return null;
            }
        }
        if (orderNumber != null) {
            List<CustomerBusiness> businesses = customerBusinessService.getBusinessesByOrderNumber(orderNumber); //get all the customer businesses by order number
            List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
            if (businessIds.size() != 0) { //find the customer business by order number
                criteria.andBusinessCustomerIdIn(businessIds);
            } else {
                return null;
            }
        }
        if (technicistId != null) {
            criteria.andAcceptOrderPeopleIdEqualTo(technicistId);
        }
        if (areaId != null) {
            List<Customer> customers = customerService.getCustomersByAreaId(areaId); //get all the customers in this area
            List<Integer> customerIds = getCustomerIdsByCustomers(customers);
            if (customerIds.size() != 0){
                List<CustomerBusiness> businesses = customerBusinessService.getBusinessByCustomerIds(customerIds);
                List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
                criteria.andBusinessCustomerIdIn(businessIds);
            } else {
                return null; //查找信息不存在
            }
        }
        if (orderStatus != null){
            List<CustomerBusiness> businesses = customerBusinessService.getBusinessesByOrderStatus(orderStatus); //get all the customer businesses by order status
            List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
            if (businessIds.size() != 0) { //find the customer business by order status
                criteria.andBusinessCustomerIdIn(businessIds);
            } else {
                return null;
            }
        }
        if (orderStartTime != null && orderEndTime != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Date start = format.parse(orderStartTime);
            Date end = format.parse(orderEndTime);
            criteria.andInstallTimeBetween(start, end);
        }
        //登录账号负责区域工单
        List<Orders> orders = getOrdersOfLoginUser(operatorId);
        List<Integer> orderIds = getOrderIdsByOrders(orders);
        criteria.andIdIn(orderIds);
        List<Orders> orders1 = ordersMapper.selectByExample(example);
        int notSend = 0;
        int send = 0;
        int collect = 0;
        int returnOrder = 0;
        int withdraw = 0;
        for (int i=0; i<orders1.size(); i++) {
            Orders order = orders1.get(i);
            int businessCustomerId = order.getBusinessCustomerId();
            CustomerBusiness business = customerBusinessService.getCustomerBusinessById(businessCustomerId); //get the customer business of this order
            String  status = business.getOrderStatus();
            if (status.equals(OrderStatus.NOT_SEND)) {
                notSend++;
            } else if (status.equals(OrderStatus.SEND)) {
                send++;
            } else if (status.equals(OrderStatus.COLLECT)) {
                collect++;
            } else if(status.equals(OrderStatus.RETURN)){
                returnOrder++;
            } else {
                withdraw++;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("notSend", notSend);
        map.put("send", send);
        map.put("collect", collect);
        map.put("return", returnOrder);
        map.put("withdraw", withdraw);
        return map;

    }

    @Override
    public Map[] getOrdersInfoAndSchedule(int operatorId) {
        List<Area> areas = areaService.getAreasByOperatorId(operatorId); //get all the areas the user manage
        Map[] data = new Map[areas.size()];
        for (int i=0; i<areas.size(); i++){
            Area area = areas.get(i);
            Map map = new HashMap();
            map.put("areaId", area.getId());
            map.put("areaName", area.getName());
            Map statusMap = getNumberOfOrderStatusByArea(area);
            map.put("orders", statusMap);
            List<Employee> techs = employeeService.getAllTechnicistsByArea(area); //get all techs in this area
            Map[] employees = new Map[techs.size()];
            for (int j=0; j<techs.size(); j++) {
                Map scheduleMap = getScheduleOfTech(techs.get(j)); //get the schedule of each yech
                employees[j] = scheduleMap;
            }
            map.put("employees", employees);
            data[i] = map;
        }
        return data;
    }

    public Map getInstalledDeviceByCustomerBusinessId(Integer id) {
        List<Orders> orders = getOrdersByCustomerBusinessId(id);
        List<Integer> orderIds = getOrderIdsByOrders(orders);
        if (orderIds.size() == 0) {
            return new HashMap();
        }else {
            List<Integer> hardwareIds = hardwareService.getGoodIdsByOrderIds(orderIds);
            List<Integer> softwareIds = softwareService.getGoodIdsByOrderIds(orderIds);
            List<Integer> permissionIds = permissionService.getGoodIdsByOrderIds(orderIds);
            Map data = new HashMap();
            List<Map> hardwareMaps = new ArrayList<>();
            for (int i=0; i<hardwareIds.size(); i++) {
                OrderHardware hardware = hardwareService.getHardwareById(hardwareIds.get(i));
                Map map = new HashMap();
                Goods good = goodsService.getGoodById(hardware.getGoodsId());
                Equipment equipment = equipmentService.getEquipmentById(good.getEquipmentId());
                map.put("modelNumber", equipment.getModelNumber());
                map.put("manufacturor", equipment.getManufacturer());
                map.put("attribute", equipment.getAttribute());
                map.put("SNCode", good.getDeviceCode());
                map.put("deviceCode", good.getNumber());
                map.put("deployMode", hardware.getDeployMode());
                map.put("upPortCode", hardware.getUpPortCode());
                map.put("downPortCode", hardware.getDownPortCode());
                map.put("publicIpv4", hardware.getPublicIpv4());
                map.put("publicIpv6", hardware.getPublicIpv6());
                map.put("privateIpv4", hardware.getPrivateIpv4());
                map.put("privateIpv6", hardware.getPrivateIpv6());
                map.put("manageIpv4", hardware.getManageIpv4());
                map.put("manageIpv6", hardware.getManageIpv6());
                map.put("platformAddress", hardware.getPlatformAddress());
                map.put("platformConnetMode", hardware.getPlatformConnectMode());
                map.put("IPSDate", hardware.getIpsUpdateTime());
                map.put("virusDate", hardware.getVirusDate());
                map.put("appUpdateDate", hardware.getAppRecognizeDate());
                map.put("URLDate", hardware.getUrlDate());
                map.put("WAFDate", hardware.getWafDate());
                hardwareMaps.add(map);
            }
            List<Map> softwareMaps = new ArrayList<>();
            for (int i=0; i<softwareIds.size(); i++) {
                OrderSoftware software = softwareService.getSoftwareById(softwareIds.get(i));
                Map map = new HashMap();
                Goods good = goodsService.getGoodById(software.getGoodsId());
                Equipment equipment = equipmentService.getEquipmentById(good.getEquipmentId());
                map.put("modelNumber", equipment.getModelNumber());
                map.put("manufacturor", equipment.getManufacturer());
                map.put("attribute", equipment.getAttribute());
                map.put("SNCode", good.getDeviceCode());
                map.put("deviceCode", good.getNumber());
                map.put("expireDate", software.getSoftwareExpireDate());
                map.put("hardwareCode", software.getHardwareCode());
                softwareMaps.add(map);
            }
            List<Map> permissionMaps = new ArrayList<>();
            for (int i=0; i< permissionIds.size(); i++) {
                OrderPermission permission = permissionService.getPermissionById(permissionIds.get(i));
                Map map = new HashMap();
                Goods good = goodsService.getGoodById(permission.getGoodsId());
                Equipment equipment = equipmentService.getEquipmentById(good.getEquipmentId());
                map.put("modelNumber", equipment.getModelNumber());
                map.put("manufacturor", equipment.getManufacturer());
                map.put("attribute", equipment.getAttribute());
                map.put("SNCode", good.getDeviceCode());
                map.put("deviceCode", good.getNumber());
                map.put("hardwareCode", permission.getHardwareCode());
                permissionMaps.add(map);
            }
            data.put("license", permissionMaps);
            data.put("software", softwareMaps);
            data.put("hardware", hardwareMaps);
            return data;
        }
    }

    private List<Orders> getOrdersByCustomerBusinessId(Integer id) {
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessCustomerIdEqualTo(id);
        return ordersMapper.selectByExample(example);
    }

    @Override
    public Map[] getInstalledDeviceByCustomerBusinessIds(List<Integer> customerBusinessIds) {
        List<Orders> orders = getOrdersByCustomerBusinessIds(customerBusinessIds);
        List<Integer> orderIds = getOrderIdsByOrders(orders);
        if (orderIds.size() == 0) {
            return new Map[0];
        }else {
            List<Integer> hardwareIds = hardwareService.getGoodIdsByOrderIds(orderIds);
            List<Integer> softwareIds = softwareService.getGoodIdsByOrderIds(orderIds);
            List<Integer> permissionIds = permissionService.getGoodIdsByOrderIds(orderIds);
            int total = hardwareIds.size() + softwareIds.size() + permissionIds.size();
            Map[] data = new Map[total];
            for (int i = 0; i < hardwareIds.size(); i++) {
                Map map = new HashMap();
                Goods good = goodsService.getGoodById(hardwareIds.get(i));
                map.put("number", good.getNumber());
                map.put("modelNumber", goodsService.getModelNumber(good));
                data[i] = map;
            }
            for (int i = 0; i < softwareIds.size(); i++) {
                Map map = new HashMap();
                Goods good = goodsService.getGoodById(softwareIds.get(i));
                map.put("number", good.getNumber());
                map.put("modelNumber", goodsService.getModelNumber(good));
                data[i + hardwareIds.size()] = map;
            }
            for (int i = 0; i < permissionIds.size(); i++) {
                Map map = new HashMap();
                Goods good = goodsService.getGoodById(permissionIds.get(i));
                map.put("number", good.getNumber());
                map.put("modelNumber", goodsService.getModelNumber(good));
                data[i + hardwareIds.size() + permissionIds.size()] = map;
            }

            return data;
        }
    }

    @Override
    public List<Orders> getOrdersUncompleteByTech(Employee employee) {
        List<Orders> orders = new ArrayList<>();
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        criteria.andAcceptOrderPeopleIdEqualTo(employee.getId());
        List<CustomerBusiness> customerBusinesses = customerBusinessService.getBusinessesNotFinished();
        if (customerBusinesses.size() == 0) {
            return orders;
        }
        List<Integer> ids = customerBusinessService.getIdsByCustomerBusinesses(customerBusinesses);
        criteria.andBusinessCustomerIdIn(ids); //find the orders that has been accept but not finished
        return ordersMapper.selectByExample(example);
    }

    @Override
    public void changeAcceptPeopleOfOrders(List<Orders> orders, int id, int handOverId, int operatorId) {
        for (int i=0; i<orders.size(); i++) {
            Orders order = orders.get(i);
            editOrdersService.addRecord(order, operatorId);
            if (order.getAcceptOrderPeopleId().equals(id)) {
                order.setAcceptOrderPeopleId(handOverId);
            }
            order.setEditTime(new Date());
            order.setOperatorId(operatorId);
            ordersMapper.updateByPrimaryKey(order);
        }
    }

    private Map getScheduleOfTech(Employee tech){ //get the schedule of the tech a week after now
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, + 7);
        Date end = calendar.getTime();
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        criteria.andAcceptOrderPeopleIdEqualTo(tech.getId());
        criteria.andInstallTimeBetween(now, end);
        List<Orders> orders = ordersMapper.selectByExample(example); //get the orders the tech hold a week
        Map[] schedules = new Map[orders.size()];
        for (int i=0; i<orders.size(); i++) {
            Orders order = orders.get(i);
            Date time = order.getInstallTime();
            int customerBusinessId = order.getBusinessCustomerId();
            CustomerBusiness customerBusiness = customerBusinessService.getCustomerBusinessById(customerBusinessId);
            int customerId = customerBusiness.getCustomerId();
            String customerName = customerService.getCustomerNameById(customerId);
            int businessId = customerBusiness.getBusinessId();
            String businessName = businessService.getBusinessNameById(businessId);
            Map scheduleMap = new HashMap();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            scheduleMap.put("time", format.format(time));
            scheduleMap.put("customerName", customerName);
            scheduleMap.put("businessName", businessName);
            schedules[i] = scheduleMap;
        }
        Map map = new HashMap();
        map.put("name", tech.getName());
        map.put("scedules", schedules);
        return map;
    }


    private Map getNumberOfOrderStatusByArea(Area area) { //该月该区域的业务工单情况
        int areaId = area.getId();
        List<Customer> customers = customerService.getCustomersByAreaId(areaId); //get all customers in this area
        List<Integer> customerIds = getCustomerIdsByCustomers(customers);
        List<CustomerBusiness> businesses = customerBusinessService.getBusinessByCustomerIds(customerIds); //get all the customer business in this area
        List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
        Map dataMap = customerBusinessService.getNumberOfOrdersStatusMonthly(businessIds);
        return dataMap;
    }




    private List<Orders> getOrdersOfLoginUser(int id) {
        List<Area> areas = areaService.getAreasByOperatorId(id); //get areas the user manager
        List<Customer> customers = customerService.getCustomersInAllAreas(areas);
        List<Integer> customerIds = getCustomerIdsByCustomers(customers);
        List<CustomerBusiness> businesses = customerBusinessService.getBusinessByCustomerIds(customerIds);
        List<Integer> businessIds = getCustomerBusinessIdByCustomerBusiness(businesses);
        List<Orders> orders = getOrdersByCustomerBusinessIds(businessIds);
        return orders;
    }

    private List<Orders> getOrdersByCustomerBusinessIds(List<Integer> businessIds){
        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessCustomerIdIn(businessIds);
        List<Orders> orders = ordersMapper.selectByExample(example);
        return orders;
    }

    private List<Integer> getOrderIdsByOrders(List<Orders> orders) {
        List<Integer> orderIds = new ArrayList<>();
        if (orders.size() != 0) {
            for (int i=0; i<orders.size(); i++) {
                int id = orders.get(i).getId();
                orderIds.add(id);
            }
        }
        return orderIds;
    }

    private List<Integer> getCustomerIdsByCustomers(List<Customer> customers) {
        List<Integer> customerIds = new ArrayList<>();
        if (customers.size() != 0) { //when the customer exist
            for (int i=0; i<customers.size(); i++) {
                int id = customers.get(i).getId();
                customerIds.add(id);
            }
        }
        return customerIds;
    }

    @Override
    public void sendOrder(int customerBusinessId, int operatorId, int technicistId, String installTime) throws ParseException {
        CustomerBusiness customerBusiness = new CustomerBusiness();
        customerBusiness.setOrderStatus(OrderStatus.SEND); //修改业务状态为已收单
        customerBusiness.setEditOperatorId(operatorId); //record the operator who change the info
        customerBusinessService.editCustomerBusiness(customerBusinessId, customerBusiness);
        Orders order = new Orders();
        order.setBusinessCustomerId(customerBusinessId);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date installDate = format.parse(installTime);
        order.setInstallTime(installDate);
        order.setAcceptOrderPeopleId(technicistId);
        order.setOperatorId(technicistId);
        Date now = new Date();
        order.setEditTime(now);
        ordersMapper.insert(order);
    }

}

