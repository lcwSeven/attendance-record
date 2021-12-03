package com.attendance.record.controller;



import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author liucaiwen
 * @date 2021/12/3
 */
@Component
public class MainController {


    public Button selectFile;
    public TextField textField;

    public Stage stage;

    public void initialize() {

        FileChooser fileChooser = new FileChooser();

        selectFile.setOnAction(e -> {

            final File file = fileChooser.showOpenDialog(stage);
            if (file != null){
                textField.setText(file.getAbsolutePath());


            }
        });

    }

    private void readExcel(File file){
        final ExcelReaderBuilder read = EasyExcel.read(file);
    }
}
