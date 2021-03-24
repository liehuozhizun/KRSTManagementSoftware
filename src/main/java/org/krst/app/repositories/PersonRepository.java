package org.krst.app.repositories;

import org.krst.app.domains.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
    @Modifying
    @Query(value = "UPDATE person_relationships SET id = :newId, name = :newName WHERE person_id = :perId AND type = :type AND id = :oldId", nativeQuery = true)
    void updateRelationshipIdAndName(String perId, String oldId, String newId, String newName, Integer type);

    @Modifying
    @Query(value = "UPDATE person_relationships SET relationship = :newRelationship WHERE person_id = :perId AND type = :type AND id = :id", nativeQuery = true)
    void updateRelationship(String perId, String id, String newRelationship, Integer type);

    @Modifying
    @Query(value = "INSERT INTO person_relationships VALUES (:perId, :id, :name, :relationship,:type)", nativeQuery = true)
    void addRelationship(String perId, String id, String name, String relationship, Integer type);

    @Modifying
    @Query(value = "DELETE FROM person_relationships WHERE person_id = :perId AND type =:type AND id = :id", nativeQuery = true)
    void removeRelationship(String perId, String id, Integer type);

    @Modifying
    @Query(value = "DELETE FROM person_relationships WHERE id = :id AND type = :type", nativeQuery = true)
    void removeRelationship(String id, Integer type);

    @Query(value = "SELECT relationship FROM person_relationships WHERE person_id = :AId AND type = :BType AND id = :BId LIMIT 1", nativeQuery = true)
    String getRelationship(String AId, String BId, Integer BType);
}
