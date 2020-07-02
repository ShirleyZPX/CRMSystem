package com.springboot.crm.controller;

import com.springboot.crm.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping("/crm/")
@RestController
public class BillController {

    @Autowired
    BillService billService;

    @GetMapping("bills/allBusinesQuota")
    public Map getAllBusinessBill(@RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        Map data = billService.getAllBusinessBill(currentPage, pageSize);
        map.put("status", "OK");
        map.put("message", "Return bills of each business");
        map.put("data", data);
        return map;
    }

    @GetMapping("bills/businesQuotaById")
    public Map getBillsHistory(@RequestParam Integer bussinessId) {
        Map<String, Object> map = new HashMap<>();
        Map data = billService.getHistoryBills(bussinessId);
        map.put("status", "OK");
        map.put("message", "Return bills of each business");
        map.put("data", data);
        return map;

    }

    @GetMapping("bills/historyBill/{id}")
    public Map getHistoryBillsOfBusiness(@PathVariable int id) {
        Map<String, Object> map = new HashMap<>();
        Map[] data = billService.getHistoryBillsOfBusiness(id);
        map.put("status", "OK");
        map.put("message", "Return bills of this business");
        map.put("data", data);
        return map;
    }

    @GetMapping("bill/billDetail/{id}")
    public Map getDetailsOfBill(@PathVariable int id) {
        Map<String, Object> map = new HashMap<>();
        Map data = billService.getDetailsOfBill(id);
        map.put("status", "OK");
        map.put("message", "Return bill information");
        map.put("data", data);
        return map;
    }

    @GetMapping("bills/selectByParams")
    public Map findBills(@RequestParam Integer areaId, @RequestParam Integer businessId, @RequestParam String billingMode, @RequestParam String  month) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        Map data = billService.findBills(areaId, businessId, billingMode, month);
        map.put("status", "OK");
        map.put("message", "Return bill information");
        map.put("data", data);
        return map;
    }

    @GetMapping("bill/download")
    public void downloadTemplate(HttpServletResponse response) throws UnsupportedEncodingException, FileNotFoundException {
        billService.downloadTemplate(response);
    }

    @PutMapping("bill/upload")
    public Map uploadOperatorBills(@RequestBody Map mapper) {
        int billId = (int) mapper.get("systemBillId");
        List<Map> operatorData = (List<Map>) mapper.get("operatorUpdateData");
        billService.uploadOperatorBills(billId, operatorData);
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        map.put("message", "Add operator bill successful");
        return map;
    }
    @PutMapping("bills/confirmBill")
    public Map confirmBills(@RequestBody Map mapper) {
        Map[] bills = (Map[]) mapper.get("bills");
        Map<String, Object> map = new HashMap<>();
        int billId = billService.confirmBill(bills);
        map.put("status", "OK");
        map.put("message", "Return the id of the bill");
        map.put("id", billId);
        return map;
    }
}
