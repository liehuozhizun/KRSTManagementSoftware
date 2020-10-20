package org.krst.app.configurations;

import org.krst.app.domains.Log;
import org.krst.app.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Logger {

    @Autowired
    private LogRepository logRepository;

    private final String placeholder = "{}";

    public void logInfo(String path, String message, String... args) {
        Arrays.stream(args).forEach(arg -> message.replace(placeholder, arg));
        logRepository.save(new Log(Log.Type.INFO, path, message));
    }

    public void logError(String path, String message, String... args) {
        Arrays.stream(args).forEach(arg -> message.replace(placeholder, arg));
        logRepository.save(new Log(Log.Type.ERROR, path, message));
    }

    public void logFetal(String path, String message, String... args) {
        Arrays.stream(args).forEach(arg -> message.replace(placeholder, arg));
        logRepository.save(new Log(Log.Type.FETAL, path, message));
    }

    public void logWarn(String path, String message, String... args) {
        Arrays.stream(args).forEach(arg -> message.replace(placeholder, arg));
        logRepository.save(new Log(Log.Type.WARN, path, message));
    }
}
