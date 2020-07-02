package com.springboot.crm.service.impl;

import com.springboot.crm.entity.*;
import com.springboot.crm.service.CustomerBusinessService;
import com.springboot.crm.service.GoodsService;
import com.springboot.crm.service.OrderService;
import com.springboot.crm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    CustomerBusinessService customerBusinessService;

    public boolean addProduct(Product product) {
        String goodCode = product.getGoodCode();
        Goods good = goodsService.getGoodsByGoodsCode(goodCode); //get goods by goods code
        if(good.getStatus() == "出库" && good.getUsingCustomerId() == null) { //物资在领货人手中
            product.setGoodsId(good.getId());
            int orderId = product.getOrderId();
            Orders order = orderService.getOrderById(orderId); //get order
            int businessId = order.getBusinessCustomerId();
            CustomerBusiness business = customerBusinessService.getCustomerBusinessById(businessId); //get customer business
            good.setUsingCustomerId(business.getCustomerId()); //set a user for the goods
            return true;
        }
        return false;
    }

}
