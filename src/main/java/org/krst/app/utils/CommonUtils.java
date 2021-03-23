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

    public static ZonedDateTime getCurrentZonedTime() {
        return ZonedDateTime.now(zoneId);
    }

    public static void alertSystemError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("系统错误");
        alert.setHeaderText("发生系统错误，请联系管理员");
        alert.setContentText("错误原因：" + errorMessage);
        alert.showAndWait();
    }

    public static void alertFeatureNotReady() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("功能尚未开放");
        alert.setHeaderText("请求的功能尚未开放");
        alert.setContentText("很抱歉，此操作/功能尚未开发完成，敬请期待！");
        alert.showAndWait();
    }
}