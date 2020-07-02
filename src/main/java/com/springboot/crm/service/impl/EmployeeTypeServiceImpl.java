package com.springboot.crm.service.impl;

import com.springboot.crm.dao.EmployeeTypeMapper;
import com.springboot.crm.entity.Employee;
import com.springboot.crm.entity.EmployeeType;
import com.springboot.crm.entity.EmployeeTypeExample;
import com.springboot.crm.entity.Type;
import com.springboot.crm.service.EmployeeTypeService;
import com.springboot.crm.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeTypeServiceImpl implements EmployeeTypeService {
    @Autowired
    EmployeeTypeMapper employeeTypeMapper;
    @Autowired
    TypeService typeService;

    @Override
    public List<Integer> getEmployeesWithRoles(List<Integer> typeIds, List<Integer> employeesIds) {
        EmployeeTypeExample example = new EmployeeTypeExample();
        EmployeeTypeExample.Criteria criteria = example.createCriteria();
        criteria.andTypeIdIn(typeIds); //with this type
        criteria.andEmployeeIdIn(employeesIds);
        List<EmployeeType> list = employeeTypeMapper.selectByExample(example); //return the columns with this role and employees
        List<Integer> employeeIdsWithRole = new ArrayList<>();
        for (int i=0; i<list.size(); i++){
            employeeIdsWithRole.add(list.get(i).getEmployeeId());
        }
        return employeeIdsWithRole;
    }

    @Override
    public void deleteByEmployee(Employee employee) { //delete all the rows of this employee
        deleteByEmployeeId(employee.getId());
    }

    @Override
    public void addEmployeeRole(Employee employee, Set<Type> roles) {
        addEmployeeRoleByEmployeeId(employee.getId(), roles);
    }

    @Override
    public void deleteByEmployeeId(int id) {
        EmployeeTypeExample example = new EmployeeTypeExample();
        EmployeeTypeExample.Criteria criteria = example.createCriteria();
        criteria.andEmployeeIdEqualTo(id);
        employeeTypeMapper.deleteByExample(example);

    }

    @Override
    public void addEmployeeRoleByEmployeeId(int id, Set<Type> roles) {
        Iterator<Type> iterator = roles.iterator();
        while (iterator.hasNext()) {
            Type role = iterator.next();
            EmployeeType employeeType = new EmployeeType();
            employeeType.setEmployeeId(id);
            employeeType.setTypeId(role.getId());
            employeeTypeMapper.insert(employeeType);
        }

    }

    @Override
    public Set<Type> getRolesByEmployee(Integer id) {
        EmployeeTypeExample example = new EmployeeTypeExample();
        EmployeeTypeExample.Criteria criteria = example.createCriteria();
        criteria.andEmployeeIdEqualTo(id);
        List<EmployeeType> types = employeeTypeMapper.selectByExample(example);
        Set<Type> set = new HashSet<>();
        for (int i=0; i<types.size(); i++) {
            int typeId = types.get(i).getTypeId();
            Type type = typeService.getTypeById(typeId);
            set.add(type);
        }
        return set;
    }

}
