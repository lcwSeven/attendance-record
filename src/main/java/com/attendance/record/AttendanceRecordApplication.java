package com.attendance.record;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author liucaiwen
 * @date 2021/12/3
 */
@SpringBootApplication
public class AttendanceRecordApplication extends Application {


    Desktop desktop = Desktop.getDesktop();
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {

        SpringApplicationBuilder builder = new SpringApplicationBuilder(AttendanceRecordApplication.class);
        applicationContext = builder.headless(false).web(WebApplicationType.NONE).run(args);
        Application.launch(AttendanceRecordApplication.class, args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

//        FileChooser fileChooser = new FileChooser();
//        Button openButton = new Button("点击选择文件");
//        Button openMultipleButton = new Button("打开");
//
//        openButton.setOnAction(
//                (final ActionEvent e) -> {
//                    File file = fileChooser.showOpenDialog(primaryStage);
//                    if (file != null) {
//                        openFile(file);
//                    }
//                });
//        openMultipleButton.setOnAction(
//                (final ActionEvent e) -> {
//                    List<File> list =
//                            fileChooser.showOpenMultipleDialog(primaryStage);
//                    if (list != null) {
//                        list.stream().forEach((file) -> {
//                            openFile(file);
//                        });
//                    }
//                });
//
//        final GridPane inputGridPane = new GridPane();
//
//        GridPane.setConstraints(openButton, 0, 0);
//        GridPane.setConstraints(openMultipleButton, 1, 0);
//        inputGridPane.setHgap(6);
//        inputGridPane.setVgap(6);
//        inputGridPane.getChildren().addAll(openButton, openMultipleButton);
//
//        final Pane rootGroup = new VBox(12);
//        rootGroup.getChildren().addAll(inputGridPane);
//        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        primaryStage.setTitle("考勤工具");
        primaryStage.setScene(new Scene(createRoot()));
        primaryStage.show();

    }

    private Parent createRoot() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main.fxml"));
        loader.setControllerFactory(applicationContext::getBean);
        return loader.load();
    }

    private void openFile(File file) {
        EventQueue.invokeLater(() -> {

            System.out.println(file.getName());
        });
    }

}
