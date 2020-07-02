package com.springboot.crm.service;

import com.springboot.crm.entity.Area;
import com.springboot.crm.entity.Employee;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmployeeService {

    Employee findUserByAccount(String account);

    Integer changePassword(String account, String oldPassword, String newPassword);

    Boolean checkPassword(String account, String password);

    boolean forgotPassword(String account);

    String provideToken(String account, String password);

    String[] getUserInfo(int id);

    boolean addEmployee(Employee employee);

    void setChangePasswordCycle(int id, String cycle);

    String getEmployeeNameById(Integer businessManagerId);

    Set getRolesById(Integer id);

    List<Employee> getAllTechnicists(List<Area> areas);


    List<Employee> getAllTechnicistsByArea(Area area);

    List<Employee> getBusinessManagerByArea(int areaId);

    Employee getEmployeeByName(String customerManager);

    boolean checkEmployeeRole(Employee customerManager, int typeId);

    List<Employee> getEmployeesByName(String name);

    List<Integer> getEmployeeIdsByEmployee(List<Employee> managers);

    Map[] getAllEmployeesMainPage();

    boolean editEmployee(int id, Employee employee);

    Map[] findUserByAccountLike(String account);

    String deleteEmployee(String account, String operatorId);

    void handOverInfo(int id, int handOverId, int operatorId);

    Map[] getHandOverList(Integer[] manageArea, Integer removeId);

    Map getEmployeeDetails(int id);

    boolean checkEmployeeTwoRole(Employee manager, int typeId1, int typeId2);

    List<Employee> getAllEmployees();
}
