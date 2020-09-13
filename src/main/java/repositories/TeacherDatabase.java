package repositories;

import domains.Teacher;
import repositories.repositories.VisitRepository;
import utils.Constants;

public class TeacherDatabase extends Database<Teacher> implements VisitRepository {

    public TeacherDatabase() {
        databaseName = Constants.TEACHER_DATABASE;
    }

}
