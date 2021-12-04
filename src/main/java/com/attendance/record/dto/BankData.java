package com.attendance.record.dto;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.localdatetime.LocalDateTimeDateConverter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @author liucaiwen
 * @date 2021/12/4
 */
public class BankData {

    @ExcelProperty("工号")
    private String workNum;
    @ExcelProperty(value = "考勤日期",converter = DateConvert.class)
    private String date;
    @ExcelProperty("时间")
    private String time;

    private String name;

    private DateTime combinedDate;

    public String getWorkNum() {
        return workNum;
    }

    public void setWorkNum(String workNum) {
        this.workNum = workNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getCombinedDate() {
        return combinedDate;
    }

    public void setCombinedDate(DateTime combinedDate) {
        this.combinedDate = combinedDate;
    }

    @Override
    public String toString() {
        return "BankData{" +
                "workNum='" + workNum + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", name='" + name + '\'' +
                ", combinedDate=" + combinedDate +
                '}';
    }
}
