package com.springboot.crm.service.impl;

import com.springboot.crm.dao.EmployeeAreaMapper;
import com.springboot.crm.dao.EmployeeMapper;
import com.springboot.crm.entity.*;
import com.springboot.crm.security.JwtTokenProvider;
import com.springboot.crm.service.*;
import com.springboot.crm.util.RandomPasswordUtil;
import com.springboot.crm.util.RolesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Value("${spring.mail.username}")
    private String from;

    private static final int REMIND_DAY = 5; //5 days before to remind change password

    private static final int DEFAULT_CHANGE_CYCLE = 30; //default change password cycle is 30 days

    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    EmployeeAreaMapper employeeAreaMapper;
    @Autowired
    AreaService areaService;
    @Autowired
    TypeService typeService;
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderService orderService;
    @Autowired
    CustomerBusinessService customerBusinessService;
    @Autowired
    EmployeeTypeService employeeTypeService;

    @Autowired
    BCryptPasswordEncoder encoder;


    @Autowired
    JavaMailSender mailSender;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider tokenProvider;

    public String provideToken(String account, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(account, password)
        );
        String token = tokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public String[] getUserInfo(int id) {
        Employee employee = employeeMapper.selectByPrimaryKey(id);
        if (employee == null) {
            return null;
        }
        String[] userInfo = new String[8];
        userInfo[0] = employee.getAccount();
        userInfo[1] = employee.getName();
        //the first-time user
        if(employee.getPasswordChangeTime() == null) {
            userInfo[2] = "true"; //first-time user
        } else {
            userInfo[2] = "false";
            LocalDate today = LocalDate.now();
            LocalDate lastChangeDay = employee.getPasswordChangeTime();
            long period = today.toEpochDay() - lastChangeDay.toEpochDay();//get the days between two dates
            int cycle = employee.getChangePasswordCycle();
            if(period >= cycle - REMIND_DAY && period < cycle) { //before 5 days password need change
                userInfo[3] = "true"; //change password reminder
                userInfo[4] = "false"; //don't force changing
            } else if(period >= cycle) { //the last day to change password
                userInfo[4] = "true"; //force changing
            }
        }

        Set<Type> roles = employee.getRolesById();
        userInfo[5]="";
        Iterator<Type> iterator = roles.iterator();
        while (iterator.hasNext()) {
            Type type = iterator.next(); //get the type of the user
            userInfo[5] += type.getName()+",";
        }

        List<Integer> areaIds = getAreasById(id);
        userInfo[6] = "";
        for (int i=0; i<areaIds.size(); i++){
            userInfo[6] += areaIds.get(i) + ",";
        }
        userInfo[7] = "";
        userInfo[7] += id;
        return userInfo;
    }

    private List<Integer> getAreasById(int id) {
        List<Integer> areaIds = new ArrayList<>();
        EmployeeAreaExample example = new EmployeeAreaExample();
        EmployeeAreaExample.Criteria criteria = example.createCriteria();
        criteria.andEmployeeIdEqualTo(id);
        List<EmployeeArea> employeeAreas = employeeAreaMapper.selectByExample(example);
        for (int i=0; i<employeeAreas.size(); i++){
            int areaId = employeeAreas.get(i).getAreaId();
            areaIds.add(areaId);
        }
        return areaIds;
    }

    @Override
    public boolean addEmployee(Employee employee) {
        try{
            employee.setPassword(encoder.encode(employee.getPassword()));
            employee.setChangePasswordCycle(DEFAULT_CHANGE_CYCLE);
            employeeMapper.insert(employee);
            List<Integer> areaIds = employee.getManageArea();
            for (int i=0; i<areaIds.size(); i++) { //insert the data into employee area table
                EmployeeArea employeeArea = new EmployeeArea();
                int areaId = areaIds.get(i);
                employeeArea.setAreaId(areaId);
                employeeArea.setEmployeeId(employee.getId());
                employeeAreaMapper.insert(employeeArea);
            }
            Set<Type> roles = employee.getRolesById();
            employeeTypeService.addEmployeeRole(employee, roles);
            return true;
        } catch (DuplicateKeyException e){
            //account exist
            return false;
        }
    }

    @Override
    public void setChangePasswordCycle(int id, String cycle) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setChangePasswordCycle(Integer.parseInt(cycle));
        employeeMapper.updateByPrimaryKeySelective(employee);
    }

    @Override
    public String getEmployeeNameById(Integer businessManagerId) {
        Employee employee = employeeMapper.selectByPrimaryKey(businessManagerId);
        if (employee == null) {
            return null;
        } else {
            String name = employee.getName();
            return name;
        }
    }

    @Override
    public Set getRolesById(Integer id) {
        Set<Type> roles = employeeTypeService.getRolesByEmployee(id);
        return roles;
    }

    @Override
    public List<Employee> getAllTechnicists(List<Area> areas) {
        List<Integer> areaIds = new ArrayList<>();
        for (int i=0; i<areas.size(); i++) {
            areaIds.add(areas.get(i).getId());
        }
        EmployeeAreaExample example = new EmployeeAreaExample();
        EmployeeAreaExample.Criteria criteria = example.createCriteria();
        criteria.andAreaIdIn(areaIds);
        List<EmployeeArea> employeeAreas = employeeAreaMapper.selectByExample(example); //get the columns with these areas
        List<Employee> employees = getTechInArea(employeeAreas);
        return employees;
    }

    private List<Employee> getTechInArea(List<EmployeeArea> employeeAreas) {
        int typeId1 = typeService.getIdByName(RolesUtil.TECH); //get the role's id
        int typeId2 = typeService.getIdByName(RolesUtil.TECH_MANAGER);
        List<Integer> typeIds = new ArrayList<>();
        typeIds.add(typeId1);
        typeIds.add(typeId2);
        List<Employee> employees = getSpecificEmployeeInArea(employeeAreas, typeIds);

        return employees;
    }

    @Override
    public List<Employee> getAllTechnicistsByArea(Area area) {
        int areaId = area.getId();
        List<Employee> employees = getTechInArea(getEmployeesByArea(areaId));
        return employees;
    }

    private List<EmployeeArea> getEmployeesByArea(int areaId){
        EmployeeAreaExample example = new EmployeeAreaExample();
        EmployeeAreaExample.Criteria criteria = example.createCriteria();
        criteria.andAreaIdEqualTo(areaId);
        List<EmployeeArea> employeeAreas = employeeAreaMapper.selectByExample(example); //get the columns with these areas
        return employeeAreas;
    }

    private List<Employee> getSpecificEmployeeInArea(List<EmployeeArea> employeeAreas, List<Integer> typeIds){ //return the specific employees in this area
        List<Integer> employeeIds = new ArrayList<>(); //get all the employees' id in these areas
        for(int j=0; j<employeeAreas.size(); j++) {
            employeeIds.add(employeeAreas.get(j).getEmployeeId());
        }
        List<Integer> employeeIdsWithRole = employeeTypeService.getEmployeesWithRoles(typeIds, employeeIds);
        List<Employee> employees = new ArrayList<>();
        if (employeeIdsWithRole.size() != 0) {
            EmployeeExample employeeExample = new EmployeeExample();
            EmployeeExample.Criteria criteria = employeeExample.createCriteria();
            criteria.andIdIn(employeeIdsWithRole);
            employees = employeeMapper.selectByExample(employeeExample);
        }
        return employees;
    }

    @Override
    public List<Employee> getBusinessManagerByArea(int areaId) {
        List<EmployeeArea> employeeAreas = getEmployeesByArea(areaId);
        int typeId1 = typeService.getIdByName(RolesUtil.SALESMAN); //get the role's id
        int typeId2 = typeService.getIdByName(RolesUtil.SALES_MANAGER); //get the role's id
        List<Integer> typeIds = new ArrayList<>();
        typeIds.add(typeId1);
        typeIds.add(typeId2);
        List<Employee> employees = getSpecificEmployeeInArea(employeeAreas, typeIds);

        return employees;

    }

    @Override
    public Employee getEmployeeByName(String customerManager) {
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andNameEqualTo(customerManager);
        List<Employee> manager = employeeMapper.selectByExample(employeeExample);
        if (manager.size() == 0){
            return null;
        } else {
            return manager.get(0);
        }

    }

    @Override
    public boolean checkEmployeeRole(Employee customerManager, int typeId) { //check if the employee is this role
        Set<Type> roles = getRolesById(customerManager.getId()); //get all the roles of the employee
        Iterator<Type> iterator = roles.iterator();
        while (iterator.hasNext()){
            Type role = iterator.next();
            if (typeId == role.getId()){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Employee> getEmployeesByName(String name) {
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        name = "%" + name +"%";
        criteria.andNameLike(name);
        return employeeMapper.selectByExample(employeeExample);
    }

    @Override
    public List<Integer> getEmployeeIdsByEmployee(List<Employee> managers) {
        List<Integer> ids = new ArrayList<>();
        for(int i=0; i<managers.size(); i++) {
            Employee manager = managers.get(i);
            ids.add(manager.getId());
        }
        return ids;
    }

    @Override
    public Map[] getAllEmployeesMainPage() {
        EmployeeAreaExample example = new EmployeeAreaExample();
        EmployeeAreaExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        List<EmployeeArea> employeeAreas = employeeAreaMapper.selectByExample(example);
        Set<String> provinces = new HashSet<>();
        for (int i=0; i<employeeAreas.size(); i++) {
            EmployeeArea employeeArea = employeeAreas.get(i);
            Area area  = areaService.getAreaById(employeeArea.getAreaId());
            provinces.add(area.getProvince());
        }
        Map[] data = new Map[provinces.size()];
        Iterator<String> iterator = provinces.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            Map map = new HashMap();
            String province = iterator.next();
            map.put("province", province);
            List<Employee> employees = getEmployeesByProvince(province);
            Map[] list = new Map[employees.size()];
            for (int i=0; i<employees.size(); i++) {
                Employee employee =  employees.get(i);
                Map employeeInfo = getEmployeeInfo(employee);
                list[i] = employeeInfo;
            }
            map.put("list", list);
            data[num] = map;
            num++;
        }
        return data;
    }

    @Override
    public boolean editEmployee(int id, Employee employee) {
        try{
            if (employee.getManageArea().size() > 0) {
                deleteEmployeeAreaByEmployeeId(id); //delete all the rows of the employee and insert the new one
                List<Integer> areaIds = employee.getManageArea();
                for (int i=0; i<areaIds.size(); i++) { //update the data into employee area table
                    EmployeeArea employeeArea = new EmployeeArea();
                    int areaId = areaIds.get(i);
                    employeeArea.setAreaId(areaId);
                    employeeArea.setEmployeeId(id);
                    employee.setEditTime(new Date());
                    employeeAreaMapper.insert(employeeArea);
                }
            }

            if (employee.getRoles().size() > 0) {
                employeeTypeService.deleteByEmployeeId(id);
                employeeTypeService.addEmployeeRoleByEmployeeId(id, employee.getRoles());
            }
            if (employee.getPassword() != null) {
                employee.setPassword(encoder.encode(employee.getPassword()));
            }
            employee.setId(id);
            employeeMapper.updateByPrimaryKeySelective(employee);
            return true;
        } catch (DuplicateKeyException e){
            //account exist
            return false;
        }
    }


    private void deleteEmployeeAreaByEmployeeId(int id) {
        EmployeeAreaExample example = new EmployeeAreaExample();
        EmployeeAreaExample.Criteria criteria = example.createCriteria();
        criteria.andEmployeeIdEqualTo(id);
        employeeAreaMapper.deleteByExample(example);
    }

    @Override
    public Map[] findUserByAccountLike(String account) {
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        account = "%" + account + "%";
        criteria.andAccountLike(account);
        List<Employee> employees = employeeMapper.selectByExample(employeeExample);
        Map[] data = new Map[employees.size()];
        for (int i=0; i<employees.size(); i++) {
            Employee employee = employees.get(i);
            Map map = new HashMap();
            map.put("id", employee.getId());
            map.put("account", employee.getAccount());
            map.put("name", employee.getName());
            data[i] = map;
        }
        return data;
    }

    @Override
    public String deleteEmployee(String account, String operatorId) {
        Employee employee = getEmployeeByAccount(account);
        if (employee == null) {
            return  "not found";
        } else {
            List<Customer> customers = customerService.getManagerByEmployee(employee); //check if the employee is a manager of a customer
            if (customers.size() != 0) {//used in customer module
                return  "error";
            }
            List<CustomerBusiness> customerBusinesses = customerBusinessService.getManagerByEmployee(employee);
            if (customerBusinesses.size() != 0) {
                return "error";
            }
            List<Orders> orders = orderService.getOrdersUncompleteByTech(employee);
            if (orders.size() !=0 || orders == null) {
                return "error";
            }
        }
        employeeTypeService.deleteByEmployee(employee); //delete all the rows of this employee
        deleteEmployeeAreaByEmployeeId(employee.getId());//delete all the area rows of this employee
        employeeMapper.deleteByPrimaryKey(employee.getId());
        return "OK";
    }

    @Override
    public void handOverInfo(int id, int handOverId, int operatorId) {
        Employee employee = employeeMapper.selectByPrimaryKey(id);
        List<Customer> customers = customerService.getManagerByEmployee(employee); //check if the employee is a manager of a customer
        if (customers.size() > 0) {
            customerService.changeManagerOFCustomers(customers, id, handOverId, operatorId);
        }
        List<CustomerBusiness> customerBusinesses = customerBusinessService.getManagerByEmployee(employee);
        if (customerBusinesses.size() > 0) {
            customerBusinessService.changeManagerOfBusiness(customerBusinesses, id, handOverId, operatorId);
        }
        List<Orders> orders = orderService.getOrdersUncompleteByTech(employee);
        if (orders.size() !=0 || orders == null) {
            orderService.changeAcceptPeopleOfOrders(orders, id, handOverId, operatorId);
        }
        employeeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Map[] getHandOverList(Integer[] manageArea, Integer removeId) {
        Set<Integer> handOverList = new HashSet<>();
        List<Employee> employees = getAllEmployees();
        for (int i=0; i<employees.size(); i++) {
            Employee employee = employees.get(i);
            if (employee.getId() != removeId) { //find the hand over list except the remove employee
                List<Integer> areaIds = getAreasById(employee.getId()); //get all the area the employee manage
                if (checkTwoListAreasSame(manageArea, areaIds)) {
                    handOverList.add(employee.getId());
                }
            }
        }
        List<Integer> adminIds = getAdmin();
        for (int i=0; i<adminIds.size(); i++) {
            int adminId = adminIds.get(i);
            if (adminId != removeId) {
                handOverList.add(adminId);//the admin also can be the hand over people
            }
        }
        Map[] data = new Map[handOverList.size()];
        Iterator<Integer> iterator = handOverList.iterator();
        int num = 0;
        while (iterator.hasNext()){
            int handOverId = iterator.next();
            Employee handOver = employeeMapper.selectByPrimaryKey(handOverId);
            Map map = new HashMap();
            map.put("id", handOver.getId());
            map.put("account", handOver.getAccount());
            map.put("name", handOver.getName());
            data[num] = map;
            num++;
        }

        return data;
    }

    @Override
    public Map getEmployeeDetails(int id) {
        Map data = new HashMap();
        Employee employee = employeeMapper.selectByPrimaryKey(id);
        data.put("id", String.valueOf(employee.getId()));
        data.put("name", employee.getName());
        data.put("gender", employee.getGender());
        data.put("phone", employee.getPhone());
        data.put("email", employee.getEmail());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        data.put("hireDate", format.format(employee.getHireDate()));
        data.put("department", employee.getDepartment());
        data.put("account", employee.getAccount());
        data.put("province", employee.getProvince());
        data.put("roles", getRolesIdByEmployee(employee));
//        data.put("roles", getRolesById(id));
        data.put("manageArea", getAreasById(id));
        return data;
    }

    @Override
    public boolean checkEmployeeTwoRole(Employee manager, int typeId1, int typeId2) {
        Set<Type> roles = getRolesById(manager.getId()); //get all the roles of the employee
        Iterator<Type> iterator = roles.iterator();
        while (iterator.hasNext()){
            Type role = iterator.next();
            if (typeId1 == role.getId() || typeId2 == role.getId()){
                return true;
            }
        }
        return false;
    }

    private List<Integer> getAdmin() {
        int typeId = typeService.getIdByName(RolesUtil.ADMIN);
        List<Integer> typeIds = new ArrayList<>();
        typeIds.add(typeId);
        List<Integer> employeeIds = employeeTypeService.getEmployeesWithRoles(typeIds, getEmployeeIdsByEmployee(getAllEmployees()));
        return employeeIds;
    }


    private boolean checkTwoListAreasSame(Integer[] manageArea, List<Integer> areaIds) {
        for (int i=0; i<manageArea.length; i++) {
            int areaId = manageArea[i];
            if(!areaIds.contains(areaId)){ //
                return false;
            }
        }
        return true;
    }

    public List<Employee> getAllEmployees() {
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andIdIsNotNull();
        return employeeMapper.selectByExample(employeeExample);
    }

    private Employee getEmployeeByAccount(String account) {
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andAccountEqualTo(account);
        List<Employee> employees = employeeMapper.selectByExample(employeeExample);
        if (employees.size() == 0) {
            return null;
        } else {
            return employees.get(0);
        }
    }

    private Map getEmployeeInfo(Employee employee) {
        Map map = new HashMap();
        map.put("id", String.valueOf(employee.getId()));
        map.put("name", employee.getName());
        map.put("account", employee.getAccount());
        List<String> roles = getRolesNameByEmployee(employee);
        map.put("roles", roles);
        List<Integer> areaIds = getAreasById(employee.getId());
        Map[] areaData = new Map[areaIds.size()];
        for (int i=0; i<areaIds.size();i++) {
            int areaId = areaIds.get(i);
            Area area = areaService.getAreaById(areaId);
            Map map1 = new HashMap();
            map1.put("id", String.valueOf(area.getId()));
            map1.put("name", area.getName());
            areaData[i] = map1;
        }
        map.put("manageArea", areaData);
        return map;
    }
    private List<Integer> getRolesIdByEmployee(Employee employee) {
        Set<Type> roles = employee.getRolesById();
        Iterator<Type> iterator = roles.iterator();
        List<Integer> rolesList = new ArrayList<>();
        while (iterator.hasNext()) {
            Type role = iterator.next();
            rolesList.add(role.getId());
        }
        return rolesList;
    }
    private List<String> getRolesNameByEmployee(Employee employee) {
        Set<Type> roles = employee.getRolesById();
        Iterator<Type> iterator = roles.iterator();
        List<String> rolesList = new ArrayList<>();
        while (iterator.hasNext()) {
            Type role = iterator.next();
            rolesList.add(role.getName());
        }
        return rolesList;
    }

    private List<Employee> getEmployeesByProvince(String province) {
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andProvinceEqualTo(province);
        return employeeMapper.selectByExample(employeeExample);
    }


    public Employee findUserByAccount(String account) {
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andAccountEqualTo(account);
        List<Employee> list = employeeMapper.selectByExample(example);
        Employee employee = null;

        // when account exist
        if(list.size() > 0)
            employee = list.get(0);

        return employee;
    }

    @Override
    public Integer changePassword(String account, String oldPassword, String newPassword) {
        Employee employee = findUserByAccount(account);
        if(employee == null) {
            return 0;
        } else {
            //find employee's password
            String password = employee.getPassword();
            if(encoder.matches(oldPassword, password)) {
                //update the password and encode
                employee.setPassword(encoder.encode(newPassword));
                LocalDate today = LocalDate.now();
                employee.setPasswordChangeTime(today);//record the password change time
                employeeMapper.updateByPrimaryKeySelective(employee);
                return employee.getId();
            } else
                return 0;
        }
    }

    @Override
    public Boolean checkPassword(String account, String password) {
        Employee employee = findUserByAccount(account);
        if(employee == null) {
            return false;
        } else {
            //find employee's password
            String userPassword = employee.getPassword();
            //(a, b) a前端传值 b数据库取值
            if(encoder.matches(password, userPassword)){
                return true;
            } else
                return false;
        }
    }

    private void sendMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(from);

        mailSender.send(message);
    }

    @Override
    public boolean forgotPassword(String account) {
        Employee employee = findUserByAccount(account);
        //account doesn't exist
        if(employee == null) {
            return false;
        } else {
            String resetPassword = RandomPasswordUtil.getRandomPassword();
            //send email to announced
            String email = employee.getEmail();
            String subject = "【密码重置】";
            String content = "您好："+"\n"+"您的账号密码已重置。"+"\n"+"重置后密码为："+ resetPassword+"\n"+"\n"+"如果不是您本人操作，请尽快联系管理员 ！";
            sendMail(email, subject, content);

            //change the new password
            employee.setPassword(encoder.encode(resetPassword));
            employeeMapper.updateByPrimaryKeySelective(employee);

            return true;
        }

    }

}
