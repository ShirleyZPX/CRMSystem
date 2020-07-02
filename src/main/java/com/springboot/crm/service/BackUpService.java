package com.springboot.crm.service;

import java.util.Date;

public interface BackUpService {
    void addBackup(Integer operatorId, Date createTime, String operation);
}
