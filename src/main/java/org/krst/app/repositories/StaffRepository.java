package org.krst.app.repositories;

import org.krst.app.domains.Relation;
import org.krst.app.domains.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    Optional<Staff> findTopByName(String name);

    @Query(value = "UPDATE staff_relationships SET id = :newId, name = :newName WHERE staff_id = :stfId AND type = :type AND id = :oldId", nativeQuery = true)
    void updateRelationshipIdAndName(String stfId, String oldId, String newId, String newName, Relation.Type type);

    @Query(value = "UPDATE staff_relationships SET relationship = :newRelationship WHERE staff_id = :stdId AND type = :type AND id = :id", nativeQuery = true)
    void updateRelationship(String stfId, String id, String newRelationship, Relation.Type type);
}
