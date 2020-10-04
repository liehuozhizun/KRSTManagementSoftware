package org.krst.app.repositories;

import org.krst.app.domains.Course;
import org.krst.app.domains.Grade;
import org.krst.app.domains.Teacher;
import org.krst.app.utils.Constants;

import java.util.ArrayList;

public class CourseDatabase extends Database<Course> {

    public CourseDatabase() {
        databaseName = Constants.COURSE_DATABASE;
    }

    public Course addTeacher(Course course, Teacher teacher) {
        if (course.getTeachers() == null) course.setTeachers(new ArrayList<>());
        course.getTeachers().add(teacher);
        return update(course);
    }

    public Course deleteTeacher(Course course, Teacher teacher) {
        if (course.getTeachers() == null) return course;
        course.getTeachers().removeIf(x -> x.getId().equals(teacher.getId()));
        return update(course);
    }

    public Course addGrade(Course course, Grade grade) {
        if (course.getGrades() == null) course.setGrades(new ArrayList<>());
        course.getGrades().add(grade);
        return update(course);
    }

    public Course deleteGrade(Course course, Grade grade) {
        if (course.getGrades() == null) return course;
        course.getGrades().removeIf(x -> x.getId().equals(grade.getId()));
        return update(course);
    }
}
