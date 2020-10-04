package org.krst.app.repositories;

import org.krst.app.domains.Grade;
import org.krst.app.utils.Constants;

public class GradeDatabase extends Database<Grade> {

    public GradeDatabase() {
        databaseName = Constants.GRADE_DATABASE;
    }

}
