package org.krst.app.repositories;

import org.krst.app.domains.Teacher;
import org.krst.app.repositories.repositories.VisitRepository;
import org.krst.app.utils.Constants;

public class TeacherDatabase extends Database<Teacher> implements VisitRepository {

    public TeacherDatabase() {
        databaseName = Constants.TEACHER_DATABASE;
    }

}
