package org.krst.app.configurations;

import org.krst.app.domains.Log;
import org.krst.app.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
public class Logger {

    @Autowired
    private LogRepository logRepository;

    private final String placeholder = "{}";
    private final int placeholderLength = placeholder.length();

    private String replacePlaceholder(String message, String[] args) {
        StringBuilder msg = new StringBuilder(message);
        Arrays.stream(args).forEach(arg -> {
            int idx = msg.indexOf(placeholder);
            msg.replace(idx, idx + placeholderLength, Objects.isNull(arg) ? "NullPointerException" : arg);
        });
        return msg.toString();
    }

    public void logInfo(String path, String message, String... args) {
        logRepository.save(new Log(Log.Type.INFO, path, replacePlaceholder(message, args)));
    }

    public void logError(String path, String message, String... args) {
        logRepository.save(new Log(Log.Type.ERROR, path, replacePlaceholder(message, args)));
    }

    public void logFetal(String path, String message, String... args) {
        logRepository.save(new Log(Log.Type.FETAL, path, replacePlaceholder(message, args)));
    }

    public void logWarn(String path, String message, String... args) {
        logRepository.save(new Log(Log.Type.WARN, path, replacePlaceholder(message, args)));
    }
}
