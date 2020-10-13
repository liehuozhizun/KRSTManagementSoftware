package org.krst.app.utils.database;

import org.krst.app.domains.*;
import org.krst.app.repositories.*;

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
    private static Database<PatronSaintDate> patronSaintDateDatabase;
    private static Database<Attribute> attributeDatabase;

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
        patronSaintDateDatabase = new PatronSaintDateDatabase();
        attributeDatabase = new AttributeDatabase();
    }

    public static Database getDatabase(DatabaseType databaseType) {
        switch (databaseType) {
            case ADMIN: return adminDatabase;
            case STUDENT: return studentDatabase;
            case TEACHER: return teacherDatabase;
            case COURSE: return courseDatabase;
            case COURSE_TEMPLATE: return courseTemplateDatabase;
            case GRADE: return gradeDatabase;
            case STAFF: return staffDatabase;
            case EVALUATION: return evaluationDatabase;
            case RELATION: return relationDatabase;
            case PATRON_SAINT_DATE: return patronSaintDateDatabase;
            case ATTRIBUTE: return attributeDatabase;
            default:
                Logger.logError("java.utils.database.DatabaseFactory", "The database instance you requested does not exist: "+databaseType);
                throw new RuntimeException("The database instance you requested does not exist: "+databaseType);
        }
    }
}
