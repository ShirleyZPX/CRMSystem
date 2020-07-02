package com.springboot.crm.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public Date getMinMonthDate(Date date) { // 获得当前月--开始日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        System.out.println(calendar.getTime());
        return calendar.getTime();
    }

    // 获得当前月--结束日期
    public Date getMaxMonthDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        System.out.println(calendar.getTime());
        return calendar.getTime();
    }

    public Date getOneYearBefore(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, -1);
        System.out.println(calendar.getTime());
        return calendar.getTime();
    }
}
