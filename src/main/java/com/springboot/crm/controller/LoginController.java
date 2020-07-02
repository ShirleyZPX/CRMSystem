package com.springboot.crm.controller;

import com.springboot.crm.service.EmployeeService;
import com.springboot.crm.service.EmployeeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RequestMapping("/crm")
@RestController
public class LoginController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/signin")
    public Map authenticateUser(@RequestBody Map<String, String> mapper) {
        Map<String, Object> map = new HashMap<>();

        String account = mapper.get("account");
        String password = mapper.get("password");
        if(employeeService.checkPassword(account, password)) {
            String token = employeeService.provideToken(account, password);
            Map<String, Object> dataMap = new HashMap<>();
            Integer id = employeeService.findUserByAccount(account).getId();
            dataMap.put("id", id);
            dataMap.put("accessToken", token);
            dataMap.put("tokenType", "Bearer");

            map.put("status", "OK");
            map.put("message", "Login Successful");
            map.put("data", dataMap);
        } else {
            //wrong account and password
            map.put("status", "Error");
            map.put("message", "Bad Credentials");
        }
      return map;
    }

    @PutMapping("/account/password")
    public Map changePassword(@RequestBody Map<String, String> mapper){
        Map<String, Object> map = new HashMap<>();
        int id = employeeService.changePassword(mapper.get("account"), mapper.get("oldPassword"), mapper.get("newPassword"));
        String cycle = mapper.get("cycle");
        if(id != 0) {
            map.put("status", "OK");
            map.put("message", "Password changed successful");
            if(cycle != "") {
                employeeService.setChangePasswordCycle(id, cycle);
            }
        } else {
            map.put("status", "Error");
            map.put("message", "Wrong password");
        }

        return map;
    }

    @PutMapping("/login/forgot")
    public Map forgotPassword(@RequestBody Map<String, String> mapper) {
        Map<String, Object> map = new HashMap<>();
        String account  = mapper.get("account");
        if(employeeService.forgotPassword(account)) {
            map.put("status", "OK");
            map.put("message", "Password reset");
        } else {
            map.put("status", "Error");
            map.put("message", "Account doesn't exist");
        }
        return map;
    }

    @GetMapping("/common/userInfo/{id}")
    public Map getUserInfo(@PathVariable int  id){
        id=37;
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        String[] userInfo = employeeService.getUserInfo(id);
        map.put("status", "OK");
        dataMap.put("account", userInfo[0]);
        dataMap.put("name", userInfo[1]);
        //first-time user
        if(userInfo[2] == "true") {
            dataMap.put("firstTimeUser", true);
            dataMap.put("passwordChangeReminder", false);
            dataMap.put("passwordChange", true);
        } else if(userInfo[3] == "true") { //password change reminder
            dataMap.put("firstTimeUser", false);
            dataMap.put("passwordChangeReminder", true);
            dataMap.put("passwordChange", false);
        } else if(userInfo[4] == "true") { //force changing
            dataMap.put("firstTimeUser", false);
            dataMap.put("passwordChangeReminder", false);
            dataMap.put("passwordChange", true);
        } else {
            dataMap.put("firstTimeUser", false);
            dataMap.put("passwordChangeReminder", false);
            dataMap.put("passwordChange", false);
        }
        String[] roles = userInfo[5].split(",");
        dataMap.put("roles", roles);
        String[] areaIds = userInfo[6].split(",");
        dataMap.put("areaId", areaIds);
        dataMap.put("id", userInfo[7]);
        map.put("data", dataMap);
        return map;
    }


}
