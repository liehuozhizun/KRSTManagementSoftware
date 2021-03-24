package org.krst.app.repositories;

import org.krst.app.domains.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository<Course, String> {
    @Modifying
    @Query(value = "UPDATE grade SET course_id = :newId WHERE course_id = :oldId", nativeQuery = true)
    void updateCourseIdInGrade(String oldId, String newId);
}
