package com.springboot.crm.util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class TemplateUtil {
    public void downloadTemplate(HttpServletResponse response, String type) throws UnsupportedEncodingException, FileNotFoundException {
        // 下载本地文件
        String fileName = "";
        String path = "src/main/resources/TemplateDownload/";
        if (type.equals("customer")) {
            fileName = "客户信息.xls".toString(); // 文件的默认保存名
        } else if (type.equals("customerBusiness")) {
            fileName = "业务信息.xlsx".toString(); // 文件的默认保存名
        } else if (type.equals("bill")) {
            fileName = "运营商上传模版.xlsx".toString(); // 文件的默认保存名
        }
        path = path + fileName;
        String downloadFileName = URLEncoder.encode(fileName,"UTF-8"); //转为中文
        // 读到流中
        InputStream inStream = new FileInputStream(path);// 文件的存放路径
        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + downloadFileName + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
