package org.krst.app.repositories;

import org.krst.app.domains.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {
    @Modifying
    @Query(value = "UPDATE teacher_relationships SET id = :newId, name = :newName WHERE teacher_id = :tecId AND type = :type AND id = :oldId", nativeQuery = true)
    void updateRelationshipIdAndName(String tecId, String oldId, String newId, String newName, Integer type);

    @Modifying
    @Query(value = "UPDATE teacher_relationships SET relationship = :newRelationship WHERE teacher_id = :tecId AND type = :type AND id = :id", nativeQuery = true)
    void updateRelationship(String tecId, String id, String newRelationship, Integer type);

    @Modifying
    @Query(value = "INSERT INTO teacher_relationships VALUES (:tecId, :id, :name, :relationship,:type)", nativeQuery = true)
    void addRelationship(String tecId, String id, String name, String relationship, Integer type);

    @Modifying
    @Query(value = "DELETE FROM teacher_relationships WHERE teacher_id = :tecId AND type =:type AND id = :id", nativeQuery = true)
    void removeRelationship(String tecId, String id, Integer type);

    @Query(value = "SELECT relationship FROM teacher_relationships WHERE teacher_id = :AId AND type = :BType AND id = :BId LIMIT 1", nativeQuery = true)
    String getRelationship(String AId, String BId, Integer BType);

    @Modifying
    @Query(value = "UPDATE course SET primary_teacher_id = :newId WHERE primary_teacher_id = :oldId", nativeQuery = true)
    void updatePrimaryTeacherIdInCourse(String oldId, String newId);

    @Modifying
    @Query(value = "UPDATE course SET secondary_teacher_id = :newId WHERE secondary_teacher_id = :oldId", nativeQuery = true)
    void updateSecondaryTeacherIdInCourse(String oldId, String newId);

    @Modifying
    @Query(value = "UPDATE course_template_teachers SET teachers_id = :newId WHERE teachers_id = :oldId", nativeQuery = true)
    void updateTeacherIdInCourseTemplate(String oldId, String newId);
}
