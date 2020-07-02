package com.springboot.crm.service;

import com.springboot.crm.entity.Employee;
import com.springboot.crm.entity.Type;

import java.util.List;
import java.util.Set;

public interface EmployeeTypeService {
    List<Integer> getEmployeesWithRoles(List<Integer> typeIds, List<Integer> employeesIds);

    void deleteByEmployee(Employee employee);

    void addEmployeeRole(Employee employee, Set<Type> roles);

    void deleteByEmployeeId(int id);

    void addEmployeeRoleByEmployeeId(int id, Set<Type> roles);

    Set<Type> getRolesByEmployee(Integer id);
}
