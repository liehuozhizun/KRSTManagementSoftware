package repositories;

import domains.Student;
import repositories.repositories.InternshipRepository;
import repositories.repositories.VisitRepository;
import utils.Constants;

public class StudentDatabase extends Database<Student> implements VisitRepository, InternshipRepository {

    public StudentDatabase() {
        databaseName = Constants.STUDENT_DATABASE;
    }

}
