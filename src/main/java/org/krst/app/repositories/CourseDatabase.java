package org.krst.app.repositories;

import org.hibernate.Session;
import org.krst.app.domains.Course;
import org.krst.app.domains.Grade;
import org.krst.app.domains.Teacher;
import org.krst.app.models.Status;
import org.krst.app.utils.Constants;
import org.krst.app.utils.HibernateUtils;

import java.util.HashSet;
import java.util.Optional;

public class CourseDatabase extends Database<Course> {

    public CourseDatabase() {
        databaseName = Constants.COURSE_DATABASE;
    }

    public Optional<Course> findById(String id) {
        try (Session session = HibernateUtils.openSession()) {
            return Optional.ofNullable(session.get(Course.class, id));
        } catch (Exception e) {
            Logger.logError(this.getClass().toString(), e.getMessage());
            return Optional.empty();
        }
    }

    public Status addTeacher(Course course, Teacher teacher) {
        if (course.getTeachers() == null) course.setTeachers(new HashSet<>());
        course.getTeachers().add(teacher);
        return update(course);
    }

    public Status deleteTeacher(Course course, Teacher teacher) {
        if (course.getTeachers() == null) return Status.SUCCESS;
        course.getTeachers().removeIf(x -> x.getId().equals(teacher.getId()));
        return update(course);
    }

    public Status addGrade(Course course, Grade grade) {
        if (course.getGrades() == null) course.setGrades(new HashSet<>());
        course.getGrades().add(grade);
        return update(course);
    }

    public Status deleteGrade(Course course, Grade grade) {
        if (course.getGrades() == null) return Status.SUCCESS;
        course.getGrades().removeIf(x -> x.getId().equals(grade.getId()));
        return update(course);
    }
}
