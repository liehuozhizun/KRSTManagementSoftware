package repositories;

import domains.CourseTemplate;
import utils.Constants;

public class CourseTemplateDatabase extends Database<CourseTemplate> {

    public CourseTemplateDatabase() {
        databaseName = Constants.COURSE_TEMPLATE_DATABASE;
    }

}
