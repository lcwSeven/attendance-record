package com.attendance.record.dto;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author liucaiwen
 * @date 2021/12/4
 */
public class TheLastData {

    @ExcelProperty("工号")
    private String workNum;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("日期")
    private String date;
    @ExcelProperty("卡中心上班时间")
    private String bankUpDate;
    @ExcelProperty("卡中心下班时间")
    private String bankDownDate;
    @ExcelProperty("公司上班时间")
    private String companyUpDate;
    @ExcelProperty("公司下班时间")
    private String companyDownDate;
    @ExcelProperty("考勤描述")
    private String desc;

    public String getWorkNum() {
        return workNum;
    }

    public void setWorkNum(String workNum) {
        this.workNum = workNum;
    }

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

    public String getBankUpDate() {
        return bankUpDate;
    }

    public void setBankUpDate(String bankUpDate) {
        this.bankUpDate = bankUpDate;
    }

    public String getBankDownDate() {
        return bankDownDate;
    }

    public void setBankDownDate(String bankDownDate) {
        this.bankDownDate = bankDownDate;
    }

    public String getCompanyUpDate() {
        return companyUpDate;
    }

    public void setCompanyUpDate(String companyUpDate) {
        this.companyUpDate = companyUpDate;
    }

    public String getCompanyDownDate() {
        return companyDownDate;
    }

    public void setCompanyDownDate(String companyDownDate) {
        this.companyDownDate = companyDownDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "TheLastData{" +
                "workNum='" + workNum + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", bankUpDate='" + bankUpDate + '\'' +
                ", bankDownDate='" + bankDownDate + '\'' +
                ", companyUpDate='" + companyUpDate + '\'' +
                ", companyDownDate='" + companyDownDate + '\'' +
                '}';
    }
}
