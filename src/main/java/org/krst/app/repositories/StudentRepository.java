package org.krst.app.repositories;

import org.krst.app.domains.Relation;
import org.krst.app.domains.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findTopByName(String name);

    @Query(value = "UPDATE student_relationships SET id = :newId, name = :newName WHERE student_id = :stdId AND type = :type AND id = :oldId", nativeQuery = true)
    void updateRelationshipIdAndName(String stdId, String oldId, String newId, String newName, Relation.Type type);

    @Query(value = "UPDATE student_relationships SET relationship = :newRelationship WHERE student_id = :stdId AND type = :type AND id = :id", nativeQuery = true)
    void updateRelationship(String stdId, String id, String newRelationship, Relation.Type type);

    @Query(value = "INSERT INTO student_relationships VALUES (:stdId, :id, :name, :relationship,:type)", nativeQuery = true)
    void addRelationship(String stdId, String id, String name, String relationship, Relation.Type type);

    @Query(value = "DELETE FROM student_relationships WHERE student_id = :stdId AND type =:type AND id = :id", nativeQuery = true)
    void removeRelationship(String stdId, String id, Relation.Type type);
}
