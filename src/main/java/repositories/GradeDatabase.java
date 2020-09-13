package repositories;

import domains.Grade;
import utils.Constants;

public class GradeDatabase extends Database<Grade> {

    public GradeDatabase() {
        databaseName = Constants.GRADE_DATABASE;
    }

}
