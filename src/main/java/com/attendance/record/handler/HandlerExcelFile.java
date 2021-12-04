package com.attendance.record.handler;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.attendance.record.dto.BankData;
import com.attendance.record.dto.CompanyData;
import com.attendance.record.dto.CompanyPersonData;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理Excel文件
 *
 * @author liucaiwen
 * @date 2021/12/4
 */
public class HandlerExcelFile {

    private static final Logger logger = LoggerFactory.getLogger(HandlerExcelFile.class);

    private static final String XLSX = "xlsx";

    private static final String XLS = "xls";

    private Map<String, String> workMap = new HashMap<>();

    private Map<String, String> nameMap = new HashMap<>();

    private List<CompanyData> companyDataList = new ArrayList<>();

    private List<BankData> bankDataList = new ArrayList<>();

    private String startDate;

    private TextArea logArea;


    public HandlerExcelFile(TextArea logArea) {
        this.logArea = logArea;

    }

    public void handlerData(File file) {
        if (!checkFile(file)) {
            printErrorLog("文件类型错误");
            return;
        }

        printLog("开始解析公司人员情况表...");
        EasyExcel.read(file, CompanyPersonData.class, new ReadListener<CompanyPersonData>() {
            @Override
            public void invoke(CompanyPersonData data, AnalysisContext analysisContext) {
                workMap.put(data.getBankWorkNum(), data.getName());
                nameMap.put(data.getName(), data.getBankWorkNum());
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                printLog("公司人员情况表解析完成...");
                logger.info("解析公司人员情况数据完成。。。");
            }
        }).sheet("公司人员情况").doRead();

        printLog("开始解析公司考勤数据表...");
        EasyExcel.read(file, CompanyData.class, new ReadListener<CompanyData>() {

            @Override
            public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
                ReadListener.super.invokeHead(headMap, context);
                if (context.readRowHolder().getRowIndex() == 0) {
                    final String title = headMap.get(0).getStringValue();
                    // 获取日期
                    final String[] titleArray = StrUtil.splitToArray(title, "至");
                    startDate = titleArray[0];
                    logger.info("start date={}", startDate);
                    printLog("获取到公司考勤数据时间="+startDate);
                }

            }

            @Override
            public void invoke(CompanyData data, AnalysisContext analysisContext) {
                if (nameMap.containsKey(data.getName())) {
                    companyDataList.add(data);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                logger.info("解析公司考勤数据完成....");
                printLog("解析公司考勤数据表完成...");
            }
        }).sheet("公司考勤数据").headRowNumber(2).doRead();

        printLog("开始解析卡中心考勤数据表...");
        EasyExcel.read(file, BankData.class, new ReadListener<BankData>() {
            @Override
            public void invoke(BankData data, AnalysisContext analysisContext) {
                if (workMap.containsKey(data.getWorkNum())) {
                    String combinedDate = data.getDate() + " " + data.getTime();
                    final DateTime parse = DateUtil.parse(combinedDate, "yyyy-MM-dd HH:mm:ss");
                    data.setName(workMap.get(data.getWorkNum()));
                    data.setCombinedDate(parse);
                    bankDataList.add(data);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                logger.info("解析卡中心考勤数据完成....");
                printLog("解析卡中心考勤数据完成...");
            }
        }).sheet("卡中心考勤数据").doRead();


    }

    private void printLog(String log) {
        Platform.runLater(() -> logArea.appendText(DateUtil.now() +" [INFO]: "+log + "\n"));
    }

    private void printErrorLog(String log) {
        Platform.runLater(() -> logArea.appendText(DateUtil.now() +" [ERROR]: "+log + "\n"));
    }



    private boolean checkFile(File file) {
        // 先要对文件进行校验
        final String type = FileUtil.getType(file);
        return type.equals(XLSX) || type.equals(XLS);

    }

    public Map<String, String> getWorkMap() {
        return workMap;
    }

    public void setWorkMap(Map<String, String> workMap) {
        this.workMap = workMap;
    }

    public Map<String, String> getNameMap() {
        return nameMap;
    }

    public void setNameMap(Map<String, String> nameMap) {
        this.nameMap = nameMap;
    }

    public List<CompanyData> getCompanyDataList() {
        return companyDataList;
    }

    public void setCompanyDataList(List<CompanyData> companyDataList) {
        this.companyDataList = companyDataList;
    }

    public List<BankData> getBankDataList() {
        return bankDataList;
    }

    public void setBankDataList(List<BankData> bankDataList) {
        this.bankDataList = bankDataList;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
