package com.attendance.record;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

/**
 * @author liucaiwen
 * @date 2021/12/3
 */
@SpringBootApplication
public class AttendanceRecordApplication extends Application {

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(AttendanceRecordApplication.class, args);
        Application.launch(AttendanceRecordApplication.class, args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
}
