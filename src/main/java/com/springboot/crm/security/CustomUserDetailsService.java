package com.springboot.crm.security;

import com.springboot.crm.dao.EmployeeMapper;
import com.springboot.crm.entity.Employee;
import com.springboot.crm.entity.EmployeeExample;
import com.springboot.crm.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    EmployeeService employeeService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String account)
            throws UsernameNotFoundException {

        return UserPrincipal.create(employeeService.findUserByAccount(account));
        //returns a UserDetails object that Spring Security uses for performing various authentication and role based validations.
    }

    @Transactional
    public UserDetails loadUserById(Integer id) {
        Employee employee = employeeMapper.selectByPrimaryKey(id);

        return UserPrincipal.create(employee);
    }
}