package com.attendance.record.dto;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author liucaiwen
 * @date 2021/12/4
 */
public class CompanyPersonData {

    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("工号")
    private String workNum;
    @ExcelProperty("卡中心工号")
    private String bankWorkNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkNum() {
        return workNum;
    }

    public void setWorkNum(String workNum) {
        this.workNum = workNum;
    }

    public String getBankWorkNum() {
        return bankWorkNum;
    }

    public void setBankWorkNum(String bankWorkNum) {
        this.bankWorkNum = bankWorkNum;
    }
}
