package org.krst.app;

import org.krst.app.views.course.AddCourseTemplate;
import org.krst.app.views.system.TestEngine;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class KRSTManagementSoftware extends AbstractJavaFxApplicationSupportExtension {
    public static void main(String[] args) {
        if(Arrays.asList(args).contains("-test=true"))
            launch(KRSTManagementSoftware.class, TestEngine.class, args);
        else
            launch(KRSTManagementSoftware.class, AddCourseTemplate.class, args);

        System.exit(0);
    }
}
