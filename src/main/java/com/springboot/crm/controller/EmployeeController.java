package com.springboot.crm.controller;

import com.springboot.crm.entity.Employee;
import com.springboot.crm.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping("/crm")
@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping("employee")
    public Map addEmployee(@RequestBody Employee employee){
        Map<String, Object> map = new HashMap<>();
        if(employeeService.addEmployee(employee)){
            map.put("status", "OK");
            map.put("message", "Add employee successful");
        } else {
            map.put("status", "Duplicated");
            map.put("message", "Account already exist");
        }
        return map;
    }
    @GetMapping("employees")
    public Map getAllEmployees() {
        Map<String, Object> map = new HashMap<>();
        List<Employee> data = employeeService.getAllEmployees();
        map.put("status", "OK");
        map.put("message", "Return all users");
        map.put("data", data);
        return map;
    }

    @GetMapping("employee/{account}")
    public Map findEmployeeByAccount(@PathVariable String account) {
        Map<String, Object> map = new HashMap<>();
        Map[] data = employeeService.findUserByAccountLike(account);
        if (data.length == 0) {
            map.put("status", "Error");
            map.put("message", "Account doesn't exist");
        } else {
            map.put("status", "OK");
            map.put("message", "Return accounts information");
            map.put("data", data);
        }
        return map;
    }

    @PutMapping("employee/update/{id}")
    public Map editEmployee(@PathVariable int id, @RequestBody Employee employee) {
        Map<String, Object> map = new HashMap<>();
        if (employeeService.editEmployee(id, employee)) {
            map.put("status", "OK");
            map.put("message", "Edit employee successful");
        } else {
            map.put("status", "Duplicated");
            map.put("message", "Account already exist");
        }
        return map;
    }

    @GetMapping("employee/selectIndex")
    public Map mainPage() {
        Map<String, Object> map = new HashMap<>();
        Map[] data = employeeService.getAllEmployeesMainPage();
        map.put("status", "OK");
        map.put("data", data);
        return map;
    }

    @DeleteMapping("employee/delete")
    public Map deleteEmployee(@RequestBody Map mapper){
        String account = (String) mapper.get("account");
        String operatorId = (String) mapper.get("operatorId");
        Map<String, Object> map = new HashMap<>();
        String info = employeeService.deleteEmployee(account, operatorId);
        if (info.equals("OK")) {
            map.put("status", "OK");
            map.put("message", "Delete employee successful");
            map.put("handOver", "false");
        } else if (info.equals("not found")) {
            map.put("status", "Error");
            map.put("handOver", "false");
            map.put("message", "Account didn't exist");
        } else {
            map.put("status", "Error");
            map.put("handOver", "true");
            map.put("message", "Still have information of the employee");
        }
        return map;
    }

    @GetMapping("employees/handOver")
    public Map getHandOverList(@RequestParam Integer[] manageArea, @RequestParam Integer removeId) {
        Map<String, Object> map = new HashMap<>();
        Map[] data = employeeService.getHandOverList(manageArea, removeId);
        map.put("status", "OK");
        map.put("message", "Return all people can be handed over successful ");
        map.put("data", data);
        return map;
    }

    @PutMapping("employee/handOver/{id}")
    public Map handOverInfo(@PathVariable int id, @RequestBody Map mapper) {
        String handOver = (String) mapper.get("handOverId");
        int handOverId = Integer.parseInt(handOver);
        String operator = (String) mapper.get("operatorId");
        int operatorId = Integer.parseInt(operator);
        Map<String, Object> map = new HashMap<>();
        employeeService.handOverInfo(id, handOverId, operatorId);
        map.put("status", "OK");
        map.put("message", "Hand over the information successful");

        return map;
    }

    @GetMapping("employee/getDetail/{id}")
    public Map getEmployeeDetail(@PathVariable int id) {
        Map<String, Object> map = new HashMap<>();
        Map data = employeeService.getEmployeeDetails(id);
        map.put("status", "OK");
        map.put("message", "Return accounts detail");
        map.put("data", data);
        return map;
    }
}
