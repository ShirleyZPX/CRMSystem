package com.springboot.crm.service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface BillService {
    Map getAllBusinessBill(int currentPage, int pageSize);

    Map[] getHistoryBillsOfBusiness(int id);

    Map getDetailsOfBill(int id);

    Map findBills(Integer areaId, Integer businessId, String billingMode, String month) throws ParseException;

    void downloadTemplate(HttpServletResponse response) throws UnsupportedEncodingException, FileNotFoundException;

    void uploadOperatorBills(int billId, List<Map> operatorData);

    int confirmBill(Map[] bills);

    Map getHistoryBills(Integer bussinessId);
}
