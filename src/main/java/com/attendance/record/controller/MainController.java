package com.attendance.record.controller;


import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.attendance.record.dto.BankData;
import com.attendance.record.dto.CompanyData;
import com.attendance.record.dto.LastBankData;
import com.attendance.record.dto.TheLastData;
import com.attendance.record.handler.HandlerExcelFile;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author liucaiwen
 * @date 2021/12/3
 */
@Component
public class MainController {


    public Button selectFile;

    public TextField textField;

    public Stage stage;

    public TextArea logArea;

    public Button clearLog;

    public Button download;

    private List<TheLastData> lastDataList = new ArrayList<>();




    public void initialize() {
        this.lastDataList.clear();
        FileChooser fileChooser = new FileChooser();

        selectFile.setOnAction(e -> {
            final File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                printLog("获取到文件filePath=" + file.getAbsolutePath());
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                textField.setText(file.getAbsolutePath());
                HandlerExcelFile handlerExcelFile = new HandlerExcelFile(logArea);
                handlerExcelFile.handlerData(file);
                final String startDate = handlerExcelFile.getStartDate();
                final String[] split = startDate.split("-");
                int year = Integer.parseInt(split[0]);
                int month = Integer.parseInt(split[1]);
                List<String> mothFullDay = getMonthFullDay(year, month);
                List<BankData> bankDataList = handlerExcelFile.getBankDataList();
                List<CompanyData> companyDataList = handlerExcelFile.getCompanyDataList();

                //处理下卡中心数据

                Map<String, String> nameMap = handlerExcelFile.getNameMap();

                final Map<String, Map<String, List<BankData>>> bankDataMap = handlerBankData(bankDataList);
                Map<String, Map<String, LastBankData>> bankLastMap = handlerLastBankData(bankDataMap);

                Map<String, Map<String, CompanyData>> companyDataMap = handlerCompanyData(companyDataList);
                List<TheLastData> lastDataList = new ArrayList<>();
                for (Map.Entry<String, String> nameData : nameMap.entrySet()) {
                    for (String date : mothFullDay) {
                        TheLastData lastData = new TheLastData();
                        lastData.setName(nameData.getKey());
                        lastData.setWorkNum(nameData.getValue());
                        lastData.setDate(date);
                        //首先根据时间取出map
                        CompanyData companyData = companyDataMap.get(date).get(nameData.getKey());
                        if (companyData == null) {
                            lastData.setCompanyDownDate("无记录");
                            lastData.setCompanyUpDate("无记录");
                            lastData.setDesc("");

                        } else {
                            lastData.setCompanyDownDate(companyData.getDownDate() == null ? "无记录" : companyData.getDownDate());
                            lastData.setCompanyUpDate(companyData.getUpDate() == null ? "无记录" : companyData.getUpDate());
                            lastData.setDesc(companyData.getDesc());
                        }

                        Map<String, LastBankData> stringLastBankDataMap = bankLastMap.get(date);
                        if (stringLastBankDataMap == null) {
                            lastData.setBankUpDate("无记录");
                            lastData.setBankDownDate("无记录");
                        } else {
                            LastBankData lastBankData = stringLastBankDataMap.get(nameData.getKey());

                            if (lastBankData == null) {
                                lastData.setBankUpDate("无记录");
                                lastData.setBankDownDate("无记录");

                            } else {
                                lastData.setBankDownDate(lastBankData.getDownDate() == null ? "无记录" : lastBankData.getDownDate());
                                lastData.setBankUpDate(lastBankData.getUpDate() == null ? "无记录" : lastBankData.getUpDate());
                            }
                        }


                        lastDataList.add(lastData);

                    }

                }


                printLog("解析完数据 count="+lastDataList.size());
                this.lastDataList = lastDataList;
                stopWatch.stop();
                final long totalTimeMillis = stopWatch.getTotalTimeMillis();
                printLog("解析考勤数据完成，耗时 = " + totalTimeMillis +" 毫秒");
                printLog("可点击【下载】按钮生成Excel。。。。");



            }


        });
        clearLog.setOnAction(event -> {
            logArea.clear();
        });


        DirectoryChooser directoryChooser = new DirectoryChooser();

        download.setOnAction(event -> {
            if (this.lastDataList.size() == 0){
                printErrorLog("没有内容可以生成，请选择模板文件进行解析。。。。");
                return;
            }
            final File file = directoryChooser.showDialog(stage);
            if (file.isFile()){
                printErrorLog("不是文件,请选择一个文件夹");
                return;
            }
            if (!file.canWrite()){
                printErrorLog("没有操作权限！");
                return;
            }

            String fileName = file.getAbsolutePath() + "/考勤数据.xlsx";

            final File gFile = new File(fileName);
            if (gFile.exists()){
                fileName = file.getAbsolutePath() + "/考勤数据" + IdUtil.simpleUUID() + ".xlsx";
            }


            printLog("正在生成fileName="+fileName + " 文件");

            try {
                 EasyExcel.write(new File(fileName),TheLastData.class)
                         .sheet("考勤数据").doWrite(lastDataList);
            }finally {
                printLog("考勤文件已生成完毕。。");
            }


        });

    }

    private Map<String, Map<String, List<BankData>>> handlerBankData(List<BankData> bankDataList) {
        Map<String, Map<String, List<BankData>>> map = new HashMap<>(16);
        for (BankData bankData : bankDataList) {
            if (map.containsKey(bankData.getDate())) {
                if (map.get(bankData.getDate()).containsKey(bankData.getName())) {
                    map.get(bankData.getDate()).get(bankData.getName()).add(bankData);
                } else {
                    List<BankData> bankNameData = new ArrayList<>();
                    bankNameData.add(bankData);
                    map.get(bankData.getDate()).put(bankData.getName(), bankNameData);
                }
            } else {
                Map<String, List<BankData>> nameBankMap = new HashMap<>(16);
                List<BankData> bankNameData = new ArrayList<>();
                bankNameData.add(bankData);
                nameBankMap.put(bankData.getName(), bankNameData);
                map.put(bankData.getDate(), nameBankMap);

            }

        }

        return map;
    }

    private Map<String, Map<String, LastBankData>> handlerLastBankData(Map<String, Map<String, List<BankData>>> map) {

        Map<String, Map<String, LastBankData>> lastBankDataMap = new HashMap<>(16);
        for (String date : map.keySet()) {
            // 判断是否包含这个key
            Map<String, List<BankData>> nameMap = map.get(date);
            if (lastBankDataMap.containsKey(date)) {
                for (String nameKey : nameMap.keySet()) {
                    List<BankData> bankDataList = nameMap.get(nameKey);
                    LastBankData lastBankData = new LastBankData();
                    lastBankData.setDate(date);
                    lastBankData.setName(nameKey);
                    bankDataList.sort((o1, o2) -> CompareUtil.compare(o1.getCombinedDate(), o2.getCombinedDate()));
                    BankData first = bankDataList.get(0);
                    BankData last = bankDataList.get(bankDataList.size() - 1);
                    //首先判断 first 和 last 是否相等
                    if (first.getTime().equals(last.getTime())) {
                        // 判断是时间点 12点之前 就表示是上班卡  12点之后就是下班卡
                        if (first.getCombinedDate().isAM()) {
                            lastBankData.setUpDate(first.getTime());
                        } else {
                            lastBankData.setDownDate(first.getTime());
                        }

                    } else {
                        // 表示不相等
                        if (first.getCombinedDate().isAM() && last.getCombinedDate().isAM()) {
                            lastBankData.setUpDate(first.getTime());
                            lastBankData.setDownDate("无记录");
                        } else if (first.getCombinedDate().isPM() && last.getCombinedDate().isPM()) {
                            lastBankData.setUpDate("无记录");
                            lastBankData.setDownDate(last.getTime());
                        } else {
                            lastBankData.setUpDate(first.getTime());
                            lastBankData.setDownDate(last.getTime());
                        }
                    }
                    lastBankDataMap.get(date).put(nameKey, lastBankData);

                }

            } else {
                Map<String, LastBankData> lastMap = new HashMap<>(16);

                for (String nameKey : nameMap.keySet()) {
                    List<BankData> bankDataList = nameMap.get(nameKey);
                    LastBankData lastBankData = new LastBankData();
                    lastBankData.setDate(date);
                    lastBankData.setName(nameKey);
                    bankDataList.sort((o1, o2) -> CompareUtil.compare(o1.getCombinedDate(), o2.getCombinedDate()));
                    BankData first = bankDataList.get(0);
                    BankData last = bankDataList.get(bankDataList.size() - 1);
//                    logger.info("first={},last={}", first.toString(), last.toString());

                    //首先判断 first 和 last 是否相等
                    if (first.getTime().equals(last.getTime())) {
                        // 判断是时间点 12点之前 就表示是上班卡  12点之后就是下班卡
                        if (first.getCombinedDate().isAM()) {
                            lastBankData.setUpDate(first.getTime());

                        } else {
                            lastBankData.setDownDate(first.getTime());
                        }

                    } else {
                        // 表示不相等
                        if (first.getCombinedDate().isAM() && last.getCombinedDate().isAM()) {
                            lastBankData.setUpDate(first.getTime());
                            lastBankData.setDownDate("无记录");
                        } else if (first.getCombinedDate().isPM() && last.getCombinedDate().isPM()) {
                            lastBankData.setUpDate("无记录");
                            lastBankData.setDownDate(last.getTime());
                        } else {
                            lastBankData.setUpDate(first.getTime());
                            lastBankData.setDownDate(last.getTime());
                        }
                    }

                    lastMap.put(nameKey, lastBankData);

                }
                lastBankDataMap.put(date, lastMap);

            }


        }

        return lastBankDataMap;
    }

    /**
     * {
     * 2021-11-01 : {
     * "刘才文": {
     * name:"刘才文",
     * upDate: "08:43:00",
     * downDate: "19:43:00"
     * <p>
     * },
     * "张三":{
     * <p>
     * }
     * }
     * }
     *
     * @param companyDataList
     * @return
     */
    private Map<String, Map<String, CompanyData>> handlerCompanyData(List<CompanyData> companyDataList) {
        Map<String, Map<String, CompanyData>> map = new HashMap<>(16);
        for (CompanyData companyData : companyDataList) {
            if (map.containsKey(companyData.getDate())) {
                map.get(companyData.getDate()).put(companyData.getName(), companyData);
            } else {
                Map<String, CompanyData> companyDataMap = new HashMap<>(16);
                companyDataMap.put(companyData.getName(), companyData);
                map.put(companyData.getDate(), companyDataMap);
            }
        }
        return map;
    }


    public List<String> getMonthFullDay(int year, int month) {
        SimpleDateFormat dateFormatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
        List<String> fullDayList = new ArrayList<>(32);
        // 获得当前日期对象
        Calendar cal = Calendar.getInstance();
        cal.clear();// 清除信息
        cal.set(Calendar.YEAR, year);
        // 1月从0开始
        cal.set(Calendar.MONTH, month - 1);
        // 当月1号
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int count = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int j = 1; j <= count; j++) {
            fullDayList.add(dateFormatYYYYMMDD.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return fullDayList;
    }

    private void printLog(String log) {
        Platform.runLater(() -> logArea.appendText(DateUtil.now() +" [INFO]: "+log + "\n"));
    }

    private void printErrorLog(String log) {
        Platform.runLater(() -> logArea.appendText(DateUtil.now() +" [ERROR]: "+log + "\n"));
    }
}
