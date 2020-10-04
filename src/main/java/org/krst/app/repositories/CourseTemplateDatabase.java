package org.krst.app.repositories;

import org.krst.app.domains.CourseTemplate;
import org.krst.app.utils.Constants;

public class CourseTemplateDatabase extends Database<CourseTemplate> {

    public CourseTemplateDatabase() {
        databaseName = Constants.COURSE_TEMPLATE_DATABASE;
    }

}
