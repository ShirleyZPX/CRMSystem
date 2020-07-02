package com.springboot.crm.service.impl;

import com.springboot.crm.dao.BackupMapper;
import com.springboot.crm.entity.Backup;
import com.springboot.crm.service.BackUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class BackUpServiceImpl implements BackUpService {
    @Autowired
    BackupMapper backupMapper;

    @Override
    public void addBackup(Integer operatorId, Date createTime, String operation) {
        Backup backup = new Backup();
        backup.setOperatorId(operatorId);
        backup.setLoginTime(createTime);
        backup.setOperation(operation);
        backupMapper.insert(backup);
    }


//    public String databasebackup(HttpServletResponse response) throws Exception{
//        String filePath="/Users/zhoupeixin/Downloads";
//        /*String fileName="批量上传模板.xlsx";*/
//        String dbName="crm";
//        try {
//            Process process = Runtime.getRuntime().exec(
//                    "mysqldump -u root -p " + dbName + " > "
//                            + filePath + "/" + dbName+new java.util.Date().getTime()
//                            + ".sql");
//            //备份的数据库名字为teacher，数据库连接和密码均为root
//            System.out.println("success!!!");
//            return "success";
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return "error";
//        }
//    }

}
