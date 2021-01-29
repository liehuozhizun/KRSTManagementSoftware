package org.krst.app.repositories;

import org.krst.app.domains.Person;
import org.krst.app.domains.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
    Optional<Person> findTopByName(String name);

    @Query(value = "UPDATE person_relationships SET id = :newId, name = :newName WHERE person_id = :perId AND type = :type AND id = :oldId", nativeQuery = true)
    void updateRelationshipIdAndName(String perId, String oldId, String newId, String name, Relation.Type type);

    @Query(value = "UPDATE person_relationships SET relationship = :newRelationship WHERE person_id = :stdId AND type = :type AND id = :id", nativeQuery = true)
    void updateRelationship(String perId, String id, String newRelationship, Relation.Type type);
}
