package org.krst.app.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtils {
    private static final ZoneId zoneId = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentTime() {
        return ZonedDateTime.now(zoneId).format(dateTimeFormatter);
    }
}