package org.krst.app.repositories;

import org.krst.app.domains.Relation;
import org.krst.app.domains.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {
    Optional<Teacher> findTopByName(String name);

    @Query(value = "UPDATE teacher_relationships SET id = :newId, name = :newName WHERE teacher_id = :tecId AND type = :type AND id = :oldId", nativeQuery = true)
    void updateRelationshipIdAndName(String tecId, String oldId, String newId, String newName, Relation.Type type);

    @Query(value = "UPDATE teacher_relationships SET relationship = :newRelationship WHERE teacher_id = :stdId AND type = :type AND id = :id", nativeQuery = true)
    void updateRelationship(String tecId, String id, String newRelationship, Relation.Type type);
}
