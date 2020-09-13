package utils.database;

import domains.*;
import repositories.*;

public class DatabaseFactory {
    private static Database<Login> adminDatabase;
    private static Database<Student> studentDatabase;
    private static Database<Teacher> teacherDatabase;
    private static Database<Course> courseDatabase;
    private static Database<CourseTemplate> courseTemplateDatabase;
    private static Database<Grade> gradeDatabase;
    private static Database<Staff> staffDatabase;
    private static Database<Evaluation> evaluationDatabase;
    private static Database<Relation> relationDatabase;
//    private static Database reminderDatabase;

    static {
        adminDatabase = new AdminDatabase();
        studentDatabase = new StudentDatabase();
        teacherDatabase = new TeacherDatabase();
        courseDatabase = new CourseDatabase();
        courseTemplateDatabase = new CourseTemplateDatabase();
        gradeDatabase = new GradeDatabase();
        staffDatabase = new StaffDatabase();
        evaluationDatabase = new EvaluationDatabase();
        relationDatabase = new RelationDatabase();
    }

    public static Database getDatabase(DatabaseType databaseType) {
        switch (databaseType) {
            case admin: return adminDatabase;
            case student: return studentDatabase;
            case teacher: return teacherDatabase;
            case course: return courseDatabase;
            case courseTemplate: return courseTemplateDatabase;
            case grade: return gradeDatabase;
            case staff: return staffDatabase;
            case evaluation: return evaluationDatabase;
            case relation: return relationDatabase;
//            case reminder: return reminderDatabase;
            default: throw new RuntimeException("The database instance you requested  does not exist: "+databaseType);
        }
    }
}
