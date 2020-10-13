package org.krst.app.utils;

import javafx.scene.control.Alert;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtils {
    private static final ZoneId zoneId = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentTime() {
        return ZonedDateTime.now(zoneId).format(dateTimeFormatter);
    }

    public static void alertSystemError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("系统错误");
        alert.setHeaderText("发生系统错误，请联系管理员");
        alert.setContentText("错误原因：" + errorMessage);
        alert.showAndWait();
    }

    public static void alertOperationError(String operation) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("操作错误");
        alert.setHeaderText("您请求的操作发生错误：" + operation);
        alert.setContentText("请求操作中断，对数据库数据无不良影响，请联系管理员解决错误！");
        alert.showAndWait();
    }
}