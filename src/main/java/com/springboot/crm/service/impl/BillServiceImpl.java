package com.springboot.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.crm.dao.SystemBillMapper;
import com.springboot.crm.dao.SystemBillTotalMapper;
import com.springboot.crm.dao.TotalBillMapper;
import com.springboot.crm.entity.*;
import com.springboot.crm.service.*;
import com.springboot.crm.util.DateUtil;
import com.springboot.crm.util.TemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    SystemBillMapper billMapper;
    @Autowired
    SystemBillTotalMapper systemBillTotalMapper;
    @Autowired
    TotalBillMapper totalBillMapper;
    @Autowired
    BusinessService businessService;
    @Autowired
    CustomerBusinessService customerBusinessService;
    @Autowired
    AreaService areaService;
    @Autowired
    CustomerService customerService;

    @Override
    public Map getAllBusinessBill(int currentPage, int pageSize) {
        Map data = new HashMap();
        TotalBillExample example = new TotalBillExample();
        TotalBillExample.Criteria criteria = example.createCriteria();
        DateUtil dateUtil = new DateUtil();
        criteria.andDateGreaterThan(dateUtil.getOneYearBefore(new Date())); //获取过去12个月
        PageHelper.startPage(currentPage, pageSize);
        List<TotalBill> totalBills = totalBillMapper.selectByExample(example);
        PageInfo<TotalBill> info = new PageInfo<>(totalBills);
        Map[] maps = new Map[info.getList().size()];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (int i=0; i<info.getList().size(); i++) {
            Map map = new HashMap();
            TotalBill totalBill = info.getList().get(i);
            map.put("bussinessId", String.valueOf(totalBill.getBusinessId()));
            map.put("bussinessName", businessService.getBusinessNameById(totalBill.getBusinessId()));
            map.put("allQuota", totalBill.getTotal());
            map.put("data", format.format(totalBill.getDate()));
            maps[i] = map;
        }
        data.put("list", maps);
        data.put("total", info.getTotal());
        return data;
    }
//
//    @Scheduled(fixedRate = 6000)
//    public void reportCurrentTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//        System.out.println("现在时间：" + dateFormat.format(new Date()));
//    }

    @Scheduled(cron = "0 2 11 * * ?")
    public void createSystemBills() {//指定为每月1号2点自动触发(cron = "0 0 2 1 * ?")
        List<Business> businesses = businessService.getAllBusinesses();
        for (int i=0; i<businesses.size(); i++) {
            Business business = businesses.get(i);
            List<CustomerBusiness> customerBusinesses = customerBusinessService.getBusinessesByBusinessId(business.getId()); //get all customer businesses of this business
            TotalBill totalBill = new TotalBill(); //每月1号插入每个业务总账单数据
            totalBill.setBusinessId(business.getId());
            totalBill.setDate(new Date());
            totalBillMapper.insert(totalBill);
            int totalId = totalBill.getId();
            int total = 0;
            for (int j = 0; j< customerBusinesses.size(); j++) {
                CustomerBusiness customerBusiness = customerBusinesses.get(j);
                if (customerBusiness.getStatus().equals("正式业务") && customerBusinessService.getUninstallBusiness(customerBusiness.getId()).size() == 0) { //只有正式业务才会有账单产生,业务结束后不再产生账单
                    String charge = businessService.getPriceByPackageId(customerBusiness.getPackageNameId());
                    total += Integer.parseInt(charge);
                    SystemBill bill = new SystemBill();
                    bill.setBusinessCustomerId(customerBusiness.getId());
                    bill.setSystemCharge(charge);
                    bill.setTime(new Date());
                    billMapper.insert(bill); //生成每个客户办理的业务的账单
                    int billId = bill.getId();

                    SystemBillTotal systemBillTotal = new SystemBillTotal();
                    systemBillTotal.setSystemBill(billId);
                    systemBillTotal.setTotalBill(totalId);
                    systemBillTotalMapper.insert(systemBillTotal); //将每一个客户办理的业务账单与该业务账单绑定
                }
            }
            totalBill.setTotal(String.valueOf(total));
            totalBillMapper.updateByPrimaryKeySelective(totalBill);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        System.out.println("现在时间：" + dateFormat.format(new Date()));
    }

    @Override
    public Map[] getHistoryBillsOfBusiness(int id) {
        TotalBillExample example = new TotalBillExample();
        TotalBillExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessIdEqualTo(id); //获得该业务的每月账单
        List<TotalBill> totalBills = totalBillMapper.selectByExample(example);
        Map[] data = new Map[totalBills.size()];
        for (int i=0; i<totalBills.size(); i++) {
            TotalBill totalBill = totalBills.get(i);
            Map map = new HashMap();
            map.put("id", totalBill.getId());
            Business business = businessService.getBusinessById(totalBill.getBusinessId());
            SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
            String date = format.format(totalBill.getDate());
            String name = business.getName()+"-"+areaService.getAreaById(business.getAreaId()).getName()+"-"+business.getchargeType()+"-"+date;
            map.put("name", name);
            data[i] = map;
        }
        return data;
    }


    @Override
    public Map getDetailsOfBill(int id) {
        Map data = new HashMap();
        List<SystemBillTotal> systemBillTotals = getSystemBillsByTotalBillId(id);//get all the customer business bills of this business in this month
        Map[] bills = new Map[systemBillTotals.size()];
        int total = 0; //记录系统出账额
        int confirmCustomer = 0; //运营商出账用户数
        int operatorBill = 0;//运营商出账额度
        int finallyBill = 0;//对账确认额度
        for (int i=0; i<systemBillTotals.size(); i++) {
            SystemBillTotal systemBillTotal = systemBillTotals.get(i);
            int systemId = systemBillTotal.getSystemBill();
            SystemBill systemBill = billMapper.selectByPrimaryKey(systemId);//get the customer business bill this month
            total += Integer.parseInt(systemBill.getSystemCharge());
            CustomerBusiness customerBusiness = customerBusinessService.getCustomerBusinessById(systemBill.getBusinessCustomerId());
            Map map = new HashMap();
            map.put("billId", systemId);
            map.put("customerId", customerBusiness.getCustomerId());
            map.put("customerName", customerService.getCustomerNameById(customerBusiness.getCustomerId()));
            Business business = businessService.getBusinessById(customerBusiness.getBusinessId());
            map.put("businessId", business.getId());
            map.put("businessName", business.getName());
            map.put("businessNumber", customerBusiness.getBusinessNumber());
            map.put("area", areaService.getAreaById(business.getAreaId()).getName());
            map.put("billingMode", business.getchargeType());
            map.put("packageName", businessService.getPackageNamebyId(customerBusiness.getPackageNameId()));
            map.put("packagePrice", businessService.getPriceByPackageId(customerBusiness.getPackageNameId()));
            map.put("systemBill", systemBill.getSystemCharge());
            if (systemBill.getOperatorChargeOffTotal() != null) {
                confirmCustomer ++;
                map.put("operatorBill", systemBill.getOperatorChargeOffTotal());
                operatorBill += Integer.parseInt(systemBill.getOperatorChargeOffTotal());
            }
            map.put("differenceReason", systemBill.getDifferenceReason());
            map.put("arrearage", systemBill.getCustomerArrearage());
            if(systemBill.getFinallyConfirmTotal() !=null) {
                map.put("finallyBill", systemBill.getFinallyConfirmTotal());
                finallyBill += Integer.parseInt(systemBill.getFinallyConfirmTotal());
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            map.put("date", format.format(systemBill.getTime()));
            bills[i] = map;
        }
        data.put("billInfo", bills);
        Map sumInfo = new HashMap();
        if (systemBillTotals.size() == confirmCustomer) {
            sumInfo.put("status", "confirmed");
        } else {
            sumInfo.put("status", "unconfirmed");
        }
        sumInfo.put("systemCustomerNumber", String.valueOf(systemBillTotals.size()));
        sumInfo.put("systemBill", String.valueOf(total));
        sumInfo.put("operatorCustomerNumber", String.valueOf(confirmCustomer));
        sumInfo.put("operatorBill", String.valueOf(operatorBill));
        sumInfo.put("finallyBill", String.valueOf(finallyBill));
        data.put("sumInfo", sumInfo);
        return data;
    }

    @Override
    public Map findBills(Integer areaId, Integer businessId, String billingMode, String month) throws ParseException {
        TotalBillExample example = new TotalBillExample();
        TotalBillExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessIdEqualTo(businessId);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        DateUtil dateUtil = new DateUtil();
        criteria.andDateBetween(dateUtil.getMinMonthDate(format.parse(month)), dateUtil.getMaxMonthDate(format.parse(month)));
        List<TotalBill> totalBills = totalBillMapper.selectByExample(example);
        Map data = new HashMap();
        if (totalBills.size() != 0) {
            TotalBill totalBill = totalBills.get(0);
            Map map = getDetailsOfBill(totalBill.getId());
            data.put("systemBillId", totalBill.getId());
            data.put("list", map.get("billInfo"));
            Map info = (Map) map.get("sumInfo");
            data.put("status", info.get("status"));
        }
        return data;
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) throws UnsupportedEncodingException, FileNotFoundException {
        TemplateUtil templateUtil = new TemplateUtil();
        templateUtil.downloadTemplate(response, "bill");
    }

    @Override
    public void uploadOperatorBills(int billId, List<Map> operatorData) {
        List<SystemBillTotal> systemBillTotals = getSystemBillsByTotalBillId(billId);
        for (int i=0; i<systemBillTotals.size(); i++) {
            SystemBillTotal systemBillTotal = systemBillTotals.get(i);
            int systemBillId = systemBillTotal.getSystemBill();
            SystemBill systemBill = billMapper.selectByPrimaryKey(systemBillId); //get the customer business bill
            CustomerBusiness customerBusiness = customerBusinessService.getCustomerBusinessById(systemBill.getBusinessCustomerId());
            Map operatorBill = getOperatorBillByCustomerBusiness(customerBusiness, operatorData);
            if (operatorBill != null) {
                systemBill.setCustomerArrearage((String) operatorBill.get("arrearage"));
                systemBill.setOperatorChargeOffTotal((String) operatorBill.get("operatorBill"));
                systemBill.setFinallyConfirmTotal((String) operatorBill.get("operatorBill")); //设置默认值
                billMapper.updateByPrimaryKeySelective(systemBill);
            }
        }
    }

    @Override
    public int confirmBill(Map[] bills) {
        int total = 0;
        for (int i=0; i<bills.length; i++) {
            Map bill = bills[i];
            int billId = (int) bill.get("billId");
            String differenceReason = (String) bill.get("differenceReason");
            String finallyBill = (String) bill.get("finallyBill");
            total += Integer.parseInt(finallyBill); //统计该业务该月的确认后的结算金额
            SystemBill systemBill = billMapper.selectByPrimaryKey(billId);
            if (!differenceReason.equals("")) {
                systemBill.setDifferenceReason(differenceReason);
            }
            if (!finallyBill.equals("")) {
                systemBill.setFinallyConfirmTotal(finallyBill);
            }
            billMapper.updateByPrimaryKeySelective(systemBill);
        }
        int systemBillId = (int) bills[0].get("billId");
        int id = getBillIdBySystemBillId(systemBillId);
        TotalBill totalBill = totalBillMapper.selectByPrimaryKey(id);
        totalBill.setFinallyTotal(String.valueOf(total));
        totalBillMapper.updateByPrimaryKeySelective(totalBill); //更新该业务该月的确认金额
        return id;
    }

    @Override
    public Map getHistoryBills(Integer bussinessId) {
        TotalBillExample example = new TotalBillExample();
        TotalBillExample.Criteria criteria = example.createCriteria();
        criteria.andBusinessIdEqualTo(bussinessId);
        List<TotalBill> totalBills = totalBillMapper.selectByExample(example);
        Map[] bills = new Map[totalBills.size()];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i=0; i<totalBills.size(); i++) {
            TotalBill totalBill = totalBills.get(i);
            Map map = new HashMap();
            map.put("id", totalBill.getId());
            if (totalBill.getFinallyTotal() == null) {
                map.put("quota", totalBill.getTotal());
                map.put("quotaStatus", "0");
            } else {
                map.put("quota", totalBill.getFinallyTotal());
                map.put("quotaStatus", "1");
            }
            map.put("data", format.format(totalBill.getDate()));
            bills[i] = map;
        }
        Map data = new HashMap();
        data.put("bussinessId", bussinessId);
        data.put("bussinessName", businessService.getBusinessNameById(bussinessId));
        data.put("list", bills);
        return data;
    }

    private int getBillIdBySystemBillId(int systemBillId) {
        SystemBillTotalExample example = new SystemBillTotalExample();
        SystemBillTotalExample.Criteria criteria = example.createCriteria();
        criteria.andSystemBillEqualTo(systemBillId);
        return systemBillTotalMapper.selectByExample(example).get(0).getTotalBill();
    }

    private Map getOperatorBillByCustomerBusiness(CustomerBusiness customerBusiness, List<Map> operatorData) {
        String businessNumber = customerBusiness.getBusinessNumber();
        String customerName = customerService.getCustomerNameById(customerBusiness.getCustomerId());
        for (int i=0; i<operatorData.size(); i++) {
            Map map = operatorData.get(i);
            String businessNum = (String) map.get("businessNumber");
            String name = (String) map.get("customerName");
            if (businessNum.equals(businessNumber) && customerName.equals(name)) {
                return map;
            }
        }
        return null;
    }

    private List<SystemBillTotal> getSystemBillsByTotalBillId(int billId) {
        SystemBillTotalExample example = new SystemBillTotalExample();
        SystemBillTotalExample.Criteria criteria = example.createCriteria();
        criteria.andTotalBillEqualTo(billId);
        List<SystemBillTotal> systemBillTotals = systemBillTotalMapper.selectByExample(example);
        return systemBillTotals;
    }


}
