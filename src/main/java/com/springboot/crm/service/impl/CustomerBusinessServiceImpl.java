package com.springboot.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.crm.dao.CustomerBusinessMapper;
import com.springboot.crm.entity.*;
import com.springboot.crm.service.*;
import com.springboot.crm.util.DateUtil;
import com.springboot.crm.util.ExcelUtil;
import com.springboot.crm.util.OrderStatus;
import com.springboot.crm.util.TemplateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CustomerBusinessServiceImpl implements CustomerBusinessService {

    @Autowired
    CustomerBusinessMapper customerBusinessMapper;
    @Autowired
    CustomerService customerService;
    @Autowired
    BusinessService businessService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    OrderService orderService;
    @Autowired
    CustomerBusinessHistoryService historyService;
    @Autowired
    AreaService areaService;

    private Integer managerId;//the id of the business manager
    private List<Integer> customerIds = new ArrayList<>(); //the ids of the customer
    private List<Integer> businessIds = new ArrayList<>();

    private final static String OFFICIAL = "正式业务";
    private final static String TRY = "试用业务";
    private final static String UNINSTALL = "拆机业务";
    private final static String TRY_END = "试用结束业务";

    private final static String INSTALL = "装机";
    private final static String TRANSFER = "移机";
    private final static String REMOVE = "拆机";

    @Override
    public Map getBusinessListOfCustomersByPage(List<Customer> customers, int status, int currentPage, int pageSize) { //支撑人员负责区域
        for (int i=0; i<customers.size(); i++) {
            customerIds.add(customers.get(i).getId());
        }
        Map<String, Object> map = getBusinessAfterPage(status, currentPage, pageSize, 2);
        return map;
    }

    private Map getBusinessAfterPage(int status, int currentPage, int pageSize, int role) { //role 0-manager 1-tech 2-支撑人员
        Map<String, Object> map = new HashMap<>();
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        if(status == 0) {
            criteria.andOrderStatusEqualTo("未派单");
        } else if(status == 1) {
            criteria.andOrderStatusEqualTo("已派单");
        } else {
            criteria.andOrderStatusEqualTo("已收单");
        }
        if(role ==0) { //if manager, get the business with this manager name
            criteria.andBusinessManagerIdEqualTo(managerId);
        } else if (role ==1) {
            criteria.andIdIn(businessIds);
        }
        else {
            criteria.andCustomerIdIn(customerIds);
        }
        example.setOrderByClause("order_accept_time asc"); //运营商工单接收日期从先到后排序
        //分页
        PageHelper.startPage(currentPage, pageSize);
        List<CustomerBusiness> businesses = customerBusinessMapper.selectByExample(example);
        PageInfo<CustomerBusiness> info = new PageInfo<>(businesses);
        map.put("list", info.getList());
        map.put("total", info.getTotal());
        return map;
    }

    public Map getBusinessListOfTechByPage(Integer id, int status, Integer currentPage, Integer pageSize) {
        List<Orders> orders = orderService.getOrdersHoldByTech(id); //get all the orders hold by the tech
        for(int i=0; i<orders.size(); i++) {
            int businessId = orders.get(i).getBusinessCustomerId();
            businessIds.add(businessId);
        }
        Map<String, Object> map = getBusinessAfterPage(status, currentPage, pageSize, 1);
        return map;
    }

    @Override
    public List<CustomerBusiness> getBusinessesByBusinessId(int businessId) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessIdEqualTo(businessId);
        List<CustomerBusiness> businesses = customerBusinessMapper.selectByExample(example);
        return businesses;
    }

    @Override
    public List<CustomerBusiness> getBusinessesByBusinessNumber(String businessNumber) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessNumberEqualTo(businessNumber);
        List<CustomerBusiness> businesses = customerBusinessMapper.selectByExample(example);
        return businesses;
    }

    @Override
    public List<CustomerBusiness> getBusinessesByBusinessManagerId(Integer businessManagerId) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessManagerIdEqualTo(businessManagerId);
        List<CustomerBusiness> businesses = customerBusinessMapper.selectByExample(example);
        return businesses;
    }

    @Override
    public List<CustomerBusiness> getBusinessesByOrderNumber(String orderNumber) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andOrderNumberEqualTo(orderNumber);
        List<CustomerBusiness> businesses = customerBusinessMapper.selectByExample(example);
        return businesses;
    }

    @Override
    public List<CustomerBusiness> getBusinessByCustomerIds(List<Integer> customerIds) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdIn(customerIds);
        List<CustomerBusiness> businesses = customerBusinessMapper.selectByExample(example);
        return businesses;
    }

    @Override
    public List<CustomerBusiness> getBusinessesByOrderStatus(String orderStatus) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andOrderStatusEqualTo(orderStatus);
        List<CustomerBusiness> businesses = customerBusinessMapper.selectByExample(example);
        return businesses;
    }

    @Override
    public Map getNumberOfOrdersStatusMonthly(List<Integer> businessIds) {
        Map<String, Object> map = new HashMap<>();
        if (businessIds.size() !=0) {
            Date now = new Date();
            int sendOrders = 0; //派单数量
            int notSend = 0; //未派单数量
            int pending = 0; //在途工单数量
            int complete = 0; //完成数量
            CustomerBusinessExample example = new CustomerBusinessExample();
            CustomerBusinessExample.Criteria criteria = example.createCriteria();
            DateUtil dateUtil = new DateUtil();
            criteria.andCreateTimeBetween(dateUtil.getMinMonthDate(now), dateUtil.getMaxMonthDate(now)); //set the start and end time
    //        criteria.andIdIn(businessIds);
            List<CustomerBusiness> businesses = customerBusinessMapper.selectByExample(example); //get all the businesses in this month and area
            for (int i = 0; i < businesses.size(); i++) {
                CustomerBusiness business = businesses.get(i);
                String status = business.getOrderStatus();
                if (status.equals(OrderStatus.NOT_SEND)) {
                    notSend++;
                } else if (status.equals(OrderStatus.SEND)) {
                    sendOrders++;
                } else if (status.equals(OrderStatus.RETURN)) {
                    complete++;
                } else if (status.equals(OrderStatus.COLLECT)) {
                    pending++;
                }
            }
            map.put("sendOrders", sendOrders);
            map.put("incomplete", notSend);
            map.put("pending", pending);
            map.put("complete", complete);
        }
        return map;
    }

    @Override
    public List<CustomerBusiness> getCustomerBusinessByBusinessId(Integer id) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessIdEqualTo(id);
        return customerBusinessMapper.selectByExample(example);
    }

    @Override
    public Map[] getCustomerBusinessInfoByCustomer(Customer customer) {
        int customerId = customer.getId();
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(customerId);
        example.setOrderByClause("create_time asc");
        List<CustomerBusiness> businesses = customerBusinessMapper.selectByExample(example);
        List<CustomerBusiness> sort = sortBusiness(businesses);
        Map[] maps = new Map[sort.size()];
        for (int i=0; i<sort.size(); i++){
            CustomerBusiness business = sort.get(i);
            Map map = new HashMap();
            map.put("business", business);
            map.put("id", business.getId());
            map.put("businessName", businessService.getBusinessNameById(business.getBusinessId()));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            map.put("businessTime", format.format(business.getCreateTime()));
            map.put("orderType", business.getOrderType());
            int packageId = business.getPackageNameId();
            String packageName = businessService.getPackageNamebyId(packageId);
            map.put("packageName", packageName);
            map.put("date", format.format(business.getCreateTime()));
            maps[i] = map;
        }
        return maps;
    }

    private long getSpecialBusinessNum(int businessId, String status, boolean date){
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessIdEqualTo(businessId);
        criteria.andStatusEqualTo(status);
        if (date){ //当月业务
            DateUtil dateUtil = new DateUtil();
            criteria.andCreateTimeBetween(dateUtil.getMinMonthDate(new Date()), dateUtil.getMaxMonthDate(new Date()));
        }
        long num = customerBusinessMapper.countByExample(example);
        return num;
    }

    @Override
    public Map[] getMainPage() {
        List<Business> businesses = businessService.getAllBusinesses();
        Map[] data = new Map[businesses.size()];
        for (int i=0; i<businesses.size(); i++){
            Business business = businesses.get(i);
            int businessId = business.getId();
            Map map = new HashMap();
            map.put("name", business.getName());
            map.put("increase", getSpecialBusinessNum(businessId, OFFICIAL, false));
            map.put("new", getSpecialBusinessNum(businessId, OFFICIAL, true));
            map.put("uninstall", getSpecialBusinessNum(businessId, UNINSTALL, true));
            map.put("try", getSpecialBusinessNum(businessId, TRY, false));
            data[i] = map;
        }
        return data;
    }

    @Override
    public boolean addCustomerBusiness(CustomerBusiness customerBusiness) {
        if (customerBusiness.getCustomerManager() != null) {
            Employee manager = employeeService.getEmployeeByName(customerBusiness.getCustomerManager());
            if (manager == null){
                return false;
            } else {
                customerBusiness.setCustomerManagerId(manager.getId());
            }
        }
        customerBusiness.setOrderStatus(OrderStatus.NOT_SEND); //默认为【未派单】
        customerBusiness.setDataStatus("新建"); //业务录入填写“新建”
        if (customerBusiness.getBusinessName() != null) {
            customerBusiness.setBusinessId(businessService.getBusinessByName(customerBusiness.getBusinessName()).getId());
        } else {
            customerBusiness.setBusinessName(businessService.getBusinessNameById(customerBusiness.getBusinessId()));
        }
        if (customerBusiness.getBusinessManager() != null) {
            customerBusiness.setBusinessManagerId(employeeService.getEmployeeByName(customerBusiness.getBusinessManager()).getId());
        } else {
            customerBusiness.setBusinessManager(employeeService.getEmployeeNameById(customerBusiness.getBusinessManagerId()));
        }
        if (customerBusiness.getCustomerManager() != null) {
            customerBusiness.setCustomerManagerId(employeeService.getEmployeeByName(customerBusiness.getCustomerManager()).getId());
        } else {
            customerBusiness.setCustomerManager(employeeService.getEmployeeNameById(customerBusiness.getCustomerManagerId()));
        }
        int customerId = 0;
        if (customerBusiness.getCustomerName() != null) {
            customerId = customerService.getCustomerByName(customerBusiness.getCustomerName()).getId();
            customerBusiness.setCustomerId(customerId);
        } else {
            customerId = customerBusiness.getCustomerId();
            customerBusiness.setCustomerName(customerService.getCustomerNameById(customerId));
        }
        Customer customer = customerService.getCustomerById(customerId);
        customer.setStatus("正式客户"); //将可删除客户修改为正式客户
        customerBusiness.setCreateTime(new Date());
        customer.setEditTime(new Date());
        customerBusinessMapper.insert(customerBusiness);
        if (customerBusiness.getStatus() == TRY_END || customerBusiness.getStatus() == UNINSTALL){ //试用结束和拆机时，查看顾客是否还有其他正式业务
            boolean status = checkCustomerHasOfficialBusiness(customerId);
            if (!status){
                customer.setStatus("可删除客户");
            }
        }

        return true;
    }

    @Override
    public Map[] findBusinessByName(int id, String customerName, String businessName) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        if(!customerName.equals("null")){
            List<Customer> customers = customerService.getCustomersByNameAndAreaOfficial(id, customerName); //get all official customers with this name in these areas
            List<Integer> customerIds = customerService.getCustomerIdsByCustomers(customers);
            if (customerIds.size()==0 ) {
                return new Map[0];
            } else {
                criteria.andCustomerIdIn(customerIds);
            }
        }
        if (!businessName.equals("null")) {
            List<Business> businesses = businessService.getBusinessesByName(businessName);
            List<Integer> businessIds = businessService.getBusinessIdsByBuinesses(businesses);
            if (businessIds.size()==0 ) {
                return new Map[0];
            } else {
                criteria.andBusinessIdIn(businessIds);
            }
        }
        List<CustomerBusiness> customerBusinesses = customerBusinessMapper.selectByExample(example);
        Map[] data = new Map[customerBusinesses.size()];
        for (int i=0; i<customerBusinesses.size(); i++){
            CustomerBusiness customerBusiness = customerBusinesses.get(i);
            Map map = new HashMap();
            map.put("id", customerBusiness.getId());
            map.put("customerName", customerService.getCustomerNameById(customerBusiness.getCustomerId()));
            map.put("businessName", businessName);
            map.put("businessNumber", customerBusiness.getBusinessNumber());
            data[i] = map;
        }
        return data;
    }

    @Override
    public boolean editCustomerBusiness(int id, CustomerBusiness customerBusiness) {
        historyService.addRecord(customerBusinessMapper.selectByPrimaryKey(id), customerBusiness.getOperatorId(), false);//将记录添加到历史表中
        if (customerBusiness.getCustomerManager()!=null) {
            Employee manager = employeeService.getEmployeeByName(customerBusiness.getCustomerManager());
            if (manager == null){
                return false;
            } else {
                customerBusiness.setCustomerManagerId(manager.getId());
            }
        }
        customerBusiness.setId(id);
        customerBusiness.setDataStatus("修改"); //业务录入填写“新建”
        customerBusiness.setEditTime(new Date());
        customerBusinessMapper.updateByPrimaryKeySelective(customerBusiness);
        return true;
    }

    @Override
    public Map[] checkDelete(int id) {
        List<CustomerBusiness> customerBusinesses = getUninstallBusiness(id);
        if (customerBusinesses.size() == 0) {
            return new Map[0];
        } else {
            List<Integer> customerBusinessIds = getIdsByCustomerBusinesses(customerBusinesses);
            Map[] data = orderService.getInstalledDeviceByCustomerBusinessIds(customerBusinessIds);
            return data;
        }
    }

    public List<CustomerBusiness> getUninstallBusiness(int id) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andOfficialIdEqualTo(id);
        criteria.andOrderTypeEqualTo(REMOVE);
        return customerBusinessMapper.selectByExample(example);
    }

    @Override
    public void deleteCustomerBusiness(int id) {
        customerBusinessMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Map[] getUnassignedBusiness(int currentPage, int pageSize) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessManagerIdIsNull();
        List<String> statuses = new ArrayList<>();
        statuses.add(OFFICIAL);
        statuses.add(TRY);
        criteria.andStatusIn(statuses);
        PageHelper.startPage(currentPage, pageSize);
        List<CustomerBusiness> customerBusinesses = customerBusinessMapper.selectByExample(example);
        PageInfo<CustomerBusiness> info = new PageInfo<>(customerBusinesses);
        List<CustomerBusiness> businesses = info.getList();
        Map[] data = new Map[businesses.size()];
        for (int i=0; i<businesses.size(); i++){
            CustomerBusiness customerBusiness = businesses.get(i);
            Map map = new HashMap();
            map.put("id", customerBusiness.getId());
            map.put("customerName", customerService.getCustomerNameById(customerBusiness.getCustomerId()));
            map.put("businessName", businessService.getBusinessNameById(customerBusiness.getBusinessId()));
            map.put("area", areaService.getAreaById(customerService.getCustomerById(customerBusiness.getCustomerId()).getAreaId()).getName());
            map.put("status", customerBusiness.getStatus());
            data[i] = map;
        }
        return data;
    }

    @Override
    public void assignBusiness(int id, int businessManagerId, int operatorId) {
        CustomerBusiness customerBusiness = customerBusinessMapper.selectByPrimaryKey(id);
        historyService.addRecord(customerBusiness, customerBusiness.getOperatorId(), false);
        customerBusiness.setBusinessManagerId(businessManagerId);
        customerBusiness.setEditOperatorId(operatorId);
    }

    @Override
    public Map[] findCustomerBusiness(String customerName, String businessName, String status, String installAddress, String orderNumber, String orderType, String orderAcceptTime, String orderReturnTime, String loadMode, String businessNumber, String bandWidth, String unicomCustomerId, String transactType, Integer packageNameId, String customerManager, String managerPhone, Integer businessManagerId, String orderStatus, Integer currentPage, Integer pageSize) throws ParseException {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        if (customerName != ""){
            List<Customer> customers = customerService.getCustomersByName(customerName);
            if (customers.size() == 0){
                return null;
            } else {
                List<Integer> customerIds = customerService.getCustomerIdsByCustomers(customers);
                criteria.andCustomerIdIn(customerIds);
            }
        }
        if (businessName != ""){
            List<Business> businesses = businessService.getBusinessesByName(businessName);
            if (businesses.size() == 0){
                return null;
            } else {
                List<Integer> businessIds = businessService.getBusinessIdsByBuinesses(businesses);
                criteria.andBusinessIdIn(businessIds);
            }
        }
        if (status != ""){
            criteria.andStatusEqualTo(status);
        }
        if (installAddress != "") {
            installAddress = "%" +installAddress+ "%";
            criteria.andInstallAddressLike(installAddress);
        }
        if (orderNumber != "") {
            orderNumber = "%" +orderNumber+ "%";
            criteria.andOrderNumberLike(orderNumber);
        }
        if (orderType != "") {
            criteria.andOrderTypeEqualTo(orderType);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (orderAcceptTime != "") {
            criteria.andOrderAcceptTimeEqualTo(format.parse(orderAcceptTime));
        }
        if (orderReturnTime != "") {
            criteria.andOrderReturnTimeEqualTo(format.parse(orderReturnTime));
        }
        if (loadMode != ""){
            criteria.andLoadModeEqualTo(loadMode);
        }
        if (businessNumber != "") {
            businessNumber = "%" +businessNumber+ "%";
            criteria.andBusinessNumberLike(businessNumber);
        }
        if (bandWidth != "") {
            bandWidth = "%" +bandWidth+ "%";
            criteria.andBandWidthLike(bandWidth);
        }
        if (unicomCustomerId != ""){
            unicomCustomerId = "%" +unicomCustomerId+ "%";
            criteria.andUnicomCustomerIdLike(unicomCustomerId);
        }
        if (transactType != "") {
            criteria.andTransactTypeEqualTo(transactType);
        }
        if (packageNameId != null) {
            criteria.andPackageNameIdEqualTo(packageNameId);
        }
        if (customerManager != "") {
            List<Employee> managers = employeeService.getEmployeesByName(customerManager);
            if (managers.size() == 0){
                return null;
            } else {
                List<Integer> managerIds = employeeService.getEmployeeIdsByEmployee(managers);
                criteria.andCustomerManagerIdIn(managerIds);
            }
        }
        if (managerPhone != "") {
            managerPhone = "%" +managerPhone+ "%";
            criteria.andManagerPhoneLike(managerPhone);
        }
        if (businessManagerId != null) {
            criteria.andBusinessManagerIdEqualTo(businessManagerId);
        }
        if (orderStatus != "") {
            criteria.andOrderStatusEqualTo(orderStatus);
        }
        PageHelper.startPage(currentPage, pageSize);
        List<CustomerBusiness> customerBusinesses = customerBusinessMapper.selectByExample(example);
        PageInfo<CustomerBusiness> info = new PageInfo<>(customerBusinesses);
        List<CustomerBusiness> dataCustomerBusiness = info.getList();
        Map[] data = new Map[dataCustomerBusiness.size()];
        for (int i=0; i<dataCustomerBusiness.size(); i++){
            Map map = new HashMap();
            CustomerBusiness business = dataCustomerBusiness.get(i);
            map.put("id", business.getId());
            map.put("customerId", business.getCustomerId());
            map.put("customerName", customerService.getCustomerNameById(business.getCustomerId()));
            map.put("businessName", businessService.getBusinessNameById(business.getBusinessId()));
            map.put("businessNumber", business.getBusinessNumber());
            map.put("status", business.getStatus());
            map.put("customerManager", employeeService.getEmployeeNameById(business.getCustomerManagerId()));
            map.put("transactType", business.getTransactType());
            data[i] = map;
        }
        return data;
    }

    @Override
    public Map findInfoByBusinessNumber(String businessNumber) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria =  example.createCriteria();
        criteria.andBusinessNumberEqualTo(businessNumber);
        List<CustomerBusiness> customerBusinesses = customerBusinessMapper.selectByExample(example);
        Map data = new HashMap();
        Customer customer = customerService.getCustomerById(customerBusinesses.get(0).getCustomerId());
        Map customerInfo = customerService.getCustomerInfo(customer);
        data.put("customer", customerInfo);
        Map[] businessInfos = new Map[customerBusinesses.size()];
        for (int i=0; i<customerBusinesses.size(); i++) {
            CustomerBusiness customerBusiness = customerBusinesses.get(i);
            Map businessInfo = getCustomerBusinessInfoById(customerBusiness.getId());
            businessInfos[i] = businessInfo;
        }
        data.put("businesses", businessInfos);
        return data;
    }

    @Override
    public void exportCustomerBusinessInfos(HttpServletResponse response) throws IOException {
        List<List<String>> excelData = new ArrayList<>();

        List<String> head = new ArrayList<>();
        head.add("客户名称");
        head.add("业务名称");
        head.add("业务状态");
        head.add("装机地址");
        head.add("运营商工单编号");
        head.add("运营商工单类型");
        head.add("运营商工单接收日期");
        head.add("运营商工单返单日期");
        head.add("业务承载方式");
        head.add("业务号码");
        head.add("业务带宽");
        head.add("联通客户ID");
        head.add("业务办理方式");
        head.add("套餐名称");
        head.add("业务资费");
        head.add("客户经理");
        head.add("客户经理电话");
        head.add("业务经理");
        head.add("工单状态");
        head.add("填写时间");
        head.add("数据状态");
        excelData.add(head);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<CustomerBusiness> customerBusinesses = getAllCustomerBusinesses();
        for (int i=0; i<customerBusinesses.size(); i++) {
            List<String> data = new ArrayList<>();
            CustomerBusiness customerBusiness = customerBusinesses.get(i);
            data.add(customerService.getCustomerNameById(customerBusiness.getCustomerId()));
            data.add(businessService.getBusinessNameById(customerBusiness.getBusinessId()));
            data.add(customerBusiness.getStatus());
            data.add(customerBusiness.getInstallAddress());
            data.add(customerBusiness.getOrderNumber());
            data.add(customerBusiness.getOrderType());
            if (customerBusiness.getOrderAcceptTime() != null){
                data.add(format.format(customerBusiness.getOrderAcceptTime()));
            } else {
                data.add("无");
            }
            if (customerBusiness.getOrderReturnTime() != null) {
                data.add(format.format(customerBusiness.getOrderReturnTime()));
            } else {
                data.add("无");
            }
            data.add(customerBusiness.getLoadMode());
            data.add(customerBusiness.getBusinessNumber());
            data.add(customerBusiness.getBandWidth());
            data.add(customerBusiness.getUnicomCustomerId());
            data.add(customerBusiness.getTransactType());
            data.add(businessService.getPackageNamebyId(customerBusiness.getPackageNameId()));
            data.add(businessService.getPriceByPackageId(customerBusiness.getPackageNameId()));
            if (customerBusiness.getCustomerManager() != null) {
                data.add(customerBusiness.getCustomerManager());
            } else {
                data.add("无");
            }
            data.add(customerBusiness.getManagerPhone());
            if (customerBusiness.getBusinessManagerId() != null) {
                data.add(employeeService.getEmployeeNameById(customerBusiness.getBusinessManagerId()));
            } else
                data.add("无");
            data.add(customerBusiness.getOrderStatus());
            data.add(format.format(customerBusiness.getCreateTime()));
            data.add(customerBusiness.getDataStatus());
            excelData.add(data);
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format1.format(new Date());
        String fileName = "业务信息+" + date;
        fileName = URLEncoder.encode(fileName,"UTF-8");
        ExcelUtil.exportExcel(response, excelData, fileName, "Sheet1", 15);
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) throws FileNotFoundException, UnsupportedEncodingException {
        TemplateUtil templateUtil = new TemplateUtil();
        templateUtil.downloadTemplate(response, "customerBusiness");
    }


    @Override
    public List<CustomerBusiness> getManagerByEmployee(Employee employee) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerManagerIdEqualTo(employee.getId());
        CustomerBusinessExample.Criteria criteria1 = example.createCriteria();
        criteria1.andBusinessManagerIdEqualTo(employee.getId());
        example.or(criteria1);
        return customerBusinessMapper.selectByExample(example);
    }

    private List<CustomerBusiness> getCustomerBusinessByTwoNames(String customerName, String businessName) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(customerService.getCustomerByName(customerName).getId());
        criteria.andBusinessIdEqualTo(businessService.getBusinessByName(businessName).getId());
        return customerBusinessMapper.selectByExample(example);
    }

    private Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(inStr);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("请上传excel文件！");
        }
        return workbook;
    }

    private List getListByExcel(InputStream in, String fileName) throws Exception {
        List list = new ArrayList<>();
        //创建Excel工作薄
        Workbook work = this.getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }

            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                List<Object> li = new ArrayList<>();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    li.add(cell);
                }
                list.add(li);
            }
        }
        work.close();
        return list;
    }

    private List<CustomerBusiness> getAllCustomerBusinesses() {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        return customerBusinessMapper.selectByExample(example);
    }

    public List<Integer> getIdsByCustomerBusinesses(List<CustomerBusiness> customerBusinesses) {
        List<Integer> customerBusinessIds = new ArrayList<>();
        for (int i=0; i<customerBusinesses.size(); i++){
            int id = customerBusinesses.get(i).getId();
            customerBusinessIds.add(id);
        }
        return customerBusinessIds;
    }

    @Override
    public List<CustomerBusiness> getBusinessesNotFinished() {
        List<String> status = new ArrayList<>();
        status.add(OrderStatus.COLLECT);
        status.add(OrderStatus.SEND);
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andOrderStatusIn(status);
        return customerBusinessMapper.selectByExample(example);
    }

    @Override
    public void changeManagerOfBusiness(List<CustomerBusiness> customerBusinesses, int id, int handOverId, int operatorId) {
        for (int i=0; i<customerBusinesses.size(); i++) {
            CustomerBusiness customerBusiness = customerBusinesses.get(i);
            historyService.addRecord(customerBusiness, customerBusiness.getOperatorId(), false);
            if (customerBusiness.getCustomerManagerId() != null) {
                if (customerBusiness.getCustomerManagerId().equals(id)) {
                    customerBusiness.setCustomerManagerId(handOverId);
                }
            }
            if (customerBusiness.getBusinessManagerId() != null) {
                if (customerBusiness.getBusinessManagerId().equals(id)) {
                    customerBusiness.setBusinessManagerId(handOverId);
                }
            }
            customerBusiness.setEditOperatorId(operatorId);
            customerBusiness.setEditTime(new Date());
            customerBusiness.setDataStatus("修改"); //业务录入填写“新建”
            customerBusinessMapper.updateByPrimaryKeySelective(customerBusiness);
        }
    }

    @Override
    public Map uploadCustomerBusinessInfo(CustomerBusiness customerBusiness) {
        Map data = new HashMap();
        List<CustomerBusiness> customerBusinesses = getCustomerBusinessByOrderNumber(customerBusiness.getOrderNumber());
        if (customerBusinesses.size() == 0) {
            Customer customer = customerService.getCustomerByName(customerBusiness.getCustomerName());
            if (customer == null) {
                data.put("info", "Customer not found");
            }
            Business business = businessService.getBusinessByName(customerBusiness.getBusinessName());
            if (business == null){
                data.put("info", "Business not found");
            }
            Employee customerManager = employeeService.getEmployeeByName(customerBusiness.getCustomerManager());
            if (customerManager == null) {
                data.put("info", "Customer Manager not found");
            }
            Employee businessManager = employeeService.getEmployeeByName(customerBusiness.getBusinessManager());
            if (businessManager == null) {
                data.put("info", "Business Manager not found");
            }
            if (customer != null && business != null && customerManager != null && businessManager != null) {
                addCustomerBusiness(customerBusiness);
                data.put("info", "add");
            }
        } else {
            CustomerBusiness customerBusiness1 = customerBusinesses.get(0);
            data.put("info", "exist");
            Map old = getCustomerBusinessInfoByCustomerBusiness(customerBusiness1);
            data.put("old", old);
            Map newCus  = getCustomerBusinessInfoByCustomerBusiness(customerBusiness);
            data.put("new", newCus);
        }
        return data;
    }

    private List<CustomerBusiness> getCustomerBusinessByOrderNumber(String orderNumber) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andOrderNumberEqualTo(orderNumber);
        return customerBusinessMapper.selectByExample(example);
    }

    private boolean checkCustomerHasOfficialBusiness(int customerId){
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(customerId);
        long num = customerBusinessMapper.countByExample(example);
        if((num % 2) == 0){ //num is double, can be delete
            return false;
        }
        return true;
    }


    private List<CustomerBusiness> sortBusiness(List<CustomerBusiness> businesses){
        List<CustomerBusiness> sort = new ArrayList<>();
        for (int i=0; i<businesses.size(); i++){
            CustomerBusiness business = businesses.get(i);
            String orderType = business.getOrderType();
            if (orderType.equals(INSTALL)) { //当订单类型为装机时，查看是否移机或拆机，若有，一起加入
                sort.add(business);
                List<CustomerBusiness> transfers = getTransferBusiness(business.getId());
                if (transfers.size() > 0) {
                    for (int t=0; t<transfers.size(); t++) {
                        CustomerBusiness transfer = transfers.get(t);
                        sort.add(transfer);
                    }
                }
                List<CustomerBusiness> uninstalls = getUninstallBusiness(business.getId());
                if (uninstalls.size() > 0) {
                    for (int u=0; u<uninstalls.size(); u++) {
                        CustomerBusiness uninstall = uninstalls.get(i);
                        sort.add(uninstall);
                    }
                }
            }
        }
        return sort;
    }

    private List<CustomerBusiness> getTransferBusiness(Integer id) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andOfficialIdEqualTo(id);
        criteria.andOrderTypeEqualTo(TRANSFER);
        return customerBusinessMapper.selectByExample(example);
    }


    @Override
    public Map getBusinessListOfManagerByPage(Integer id, int status, Integer currentPage, Integer pageSize) { //业务经理负责的业务
        managerId = id;
        Map<String, Object> map = getBusinessAfterPage(status, currentPage, pageSize, 0);
        return map;
    }

    private Map getCustomerBusinessInfoByCustomerBusiness(CustomerBusiness customerBusiness) {
        Map data = new HashMap();
        data.put("customerId", customerBusiness.getCustomerId());
        data.put("businessId", customerBusiness.getBusinessId());
        data.put("customerName", customerBusiness.getCustomerName());
        data.put("businessName", customerBusiness.getBusinessName());
        data.put("status", customerBusiness.getStatus());
        data.put("installAddress", customerBusiness.getInstallAddress());
        data.put("orderNumber", customerBusiness.getOrderNumber());
        data.put("orderType", customerBusiness.getOrderType());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        data.put("orderAcceptTime", format.format(customerBusiness.getOrderAcceptTime()));
        data.put("orderReturnTime", format.format(customerBusiness.getOrderReturnTime()));
        data.put("loadMode", customerBusiness.getLoadMode());
        data.put("businessNumber", customerBusiness.getBusinessNumber());
        data.put("bandWidth", customerBusiness.getBandWidth());
        data.put("unicomCustomerId", customerBusiness.getUnicomCustomerId());
        data.put("transactType", customerBusiness.getTransactType());
        data.put("packageNameId", customerBusiness.getPackageNameId());
        data.put("packageName", businessService.getPackageNamebyId(customerBusiness.getPackageNameId()));
        data.put("price", businessService.getPriceByPackageId(customerBusiness.getPackageNameId()));
        if (customerBusiness.getCustomerManager() != null) {
            data.put("customerManager", customerBusiness.getCustomerManager());
            data.put("customerManagerId", customerBusiness.getCustomerManagerId());
        }
        data.put("managerPhone", customerBusiness.getManagerPhone());
        if (customerBusiness.getBusinessManagerId() != null) {
            data.put("businessManagerId", customerBusiness.getBusinessManagerId());
            data.put("businessManager", employeeService.getEmployeeNameById(customerBusiness.getBusinessManagerId()));
        }
        data.put("orderStatus", customerBusiness.getOrderStatus());
        data.put("operator", employeeService.getEmployeeNameById(customerBusiness.getOperatorId()));
        data.put("operatorId", customerBusiness.getOperatorId());
        data.put("dataStatus", customerBusiness.getDataStatus());
        return data;
    }

    @Override
    public Map getCustomerBusinessInfoById(Integer id) {
        CustomerBusiness customerBusiness = customerBusinessMapper.selectByPrimaryKey(id);
        return  getCustomerBusinessInfoByCustomerBusiness(customerBusiness);
    }

    @Override
    public List<CustomerBusiness> getBusinessByCustomerId(int id) {
        CustomerBusinessExample example = new CustomerBusinessExample();
        CustomerBusinessExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(id);
        List<CustomerBusiness> businesses = customerBusinessMapper.selectByExample(example);
        return businesses;
    }

    @Override
    public String[] getCustomerBusinessNameById(Integer businessCustomerId) {
        String[] names = new String[2];
        CustomerBusiness customerBusiness = customerBusinessMapper.selectByPrimaryKey(businessCustomerId);
        int customerId = customerBusiness.getCustomerId();
        names[0] =customerService.getCustomerNameById(customerId); //get the customer name
        int businessId = customerBusiness.getBusinessId();
        names[1] = businessService.getBusinessNameById(businessId); //get the business name
        return names;
    }

    @Override
    public CustomerBusiness getCustomerBusinessById(int customerBusinessId) {
        CustomerBusiness customerBusiness = customerBusinessMapper.selectByPrimaryKey(customerBusinessId);
        return customerBusiness;
    }
}
