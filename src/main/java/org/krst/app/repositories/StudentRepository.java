package org.krst.app.repositories;

import org.krst.app.domains.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    @Modifying
    @Query(value = "UPDATE student_relationships SET id = :newId, name = :newName WHERE student_id = :stdId AND type = :type AND id = :oldId", nativeQuery = true)
    void updateRelationshipIdAndName(String stdId, String oldId, String newId, String newName, Integer type);

    @Modifying
    @Query(value = "UPDATE student_relationships SET relationship = :newRelationship WHERE student_id = :stdId AND type = :type AND id = :id", nativeQuery = true)
    void updateRelationship(String stdId, String id, String newRelationship, Integer type);

    @Modifying
    @Query(value = "INSERT INTO student_relationships VALUES (:stdId, :id, :name, :relationship,:type)", nativeQuery = true)
    void addRelationship(String stdId, String id, String name, String relationship, Integer type);

    @Modifying
    @Query(value = "DELETE FROM student_relationships WHERE student_id = :stdId AND type =:type AND id = :id", nativeQuery = true)
    void removeRelationship(String stdId, String id, Integer type);

    @Query(value = "SELECT relationship FROM student_relationships WHERE student_id = :AId AND type = :BType AND id = :BId LIMIT 1", nativeQuery = true)
    String getRelationship(String AId, String BId, Integer BType);
}
