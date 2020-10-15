package org.krst.app.repositories;

import org.krst.app.domains.Grade;
import org.krst.app.domains.Student;
import org.krst.app.repositories.repositories.InternshipRepository;
import org.krst.app.utils.Constants;
import org.krst.app.repositories.repositories.VisitRepository;

import java.util.List;

public class StudentDatabase extends Database<Student> implements VisitRepository, InternshipRepository {

    public StudentDatabase() {
        databaseName = Constants.STUDENT_DATABASE;
    }

}
