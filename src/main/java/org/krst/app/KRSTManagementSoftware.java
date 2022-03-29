package org.krst.app;

import org.krst.app.views.system.Login;
import org.krst.app.views.system.TestEngine;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Arrays;

@SpringBootApplication
public class KRSTManagementSoftware extends AbstractJavaFxApplicationSupportExtension implements ApplicationContextAware {
    public static void main(String[] args) {
        if(Arrays.asList(args).contains("-test=true"))
            launch(KRSTManagementSoftware.class, TestEngine.class, args);
        else
            launch(KRSTManagementSoftware.class, Login.class, args);

        System.exit(0);
    }

    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (null == context) return null;
        return context.getBean(clazz);
    }
}
