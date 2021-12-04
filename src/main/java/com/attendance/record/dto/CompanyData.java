package com.attendance.record.dto;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author liucaiwen
 * @date 2021/12/4
 */

public class CompanyData {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("日期")
    private String date;

    @ExcelProperty("上班打卡时间")
    private String upDate;

    @ExcelProperty("下班打卡时间")
    private String downDate;

    @ExcelProperty("考勤描述")
    private String desc;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUpDate() {
        return upDate;
    }

    public void setUpDate(String upDate) {
        this.upDate = upDate;
    }

    public String getDownDate() {
        return downDate;
    }

    public void setDownDate(String downDate) {
        this.downDate = downDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
