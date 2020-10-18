package org.krst.app;

import org.krst.app.views.course.AddCourseTemplate;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KRSTManagementSoftware extends AbstractJavaFxApplicationSupportExtension {
    public static void main(String[] args) {
        launch(KRSTManagementSoftware.class, AddCourseTemplate.class, args);
    }
}
