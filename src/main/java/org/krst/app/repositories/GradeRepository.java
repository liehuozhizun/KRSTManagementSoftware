package org.krst.app.repositories;

import org.krst.app.domains.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface GradeRepository extends JpaRepository<Grade, Integer> {
    @Modifying
    @Query(value = "INSERT INTO student_grades (student_id, grades_id) VALUES (:stdId, :grdId)", nativeQuery = true)
    void addGradeStudent(String stdId, Integer grdId);

    @Modifying
    @Query(value = "INSERT INTO course_grades (course_id, grades_id) VALUES (:crsId, :grdId)", nativeQuery = true)
    void addGradeCourse(String crsId, Integer grdId);

    @Modifying
    @Query(value = "DELETE FROM student_grades WHERE student_id = :stdId AND grades_id = :grdId", nativeQuery = true)
    void removeGradeStudent(String stdId, Integer grdId);

    @Modifying
    @Query(value = "DELETE FROM course_grades WHERE course_id = :crsId AND grades_id = :grdId", nativeQuery = true)
    void removeGradeCourse(String crsId, Integer grdId);
}
