package org.krst.app.repositories;

import org.krst.app.domains.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    @Modifying
    @Query(value = "UPDATE staff_relationships SET id = :newId, name = :newName WHERE staff_id = :stfId AND type = :type AND id = :oldId", nativeQuery = true)
    void updateRelationshipIdAndName(String stfId, String oldId, String newId, String newName, Integer type);

    @Modifying
    @Query(value = "UPDATE staff_relationships SET relationship = :newRelationship WHERE staff_id = :stfId AND type = :type AND id = :id", nativeQuery = true)
    void updateRelationship(String stfId, String id, String newRelationship, Integer type);

    @Modifying
    @Query(value = "INSERT INTO staff_relationships VALUES (:stfId, :id, :name, :relationship,:type)", nativeQuery = true)
    void addRelationship(String stfId, String id, String name, String relationship, Integer type);

    @Modifying
    @Query(value = "DELETE FROM staff_relationships WHERE staff_id = :stfId AND type =:type AND id = :id", nativeQuery = true)
    void removeRelationship(String stfId, String id, Integer type);

    @Modifying
    @Query(value = "DELETE FROM staff_relationships WHERE id = :id AND type = :type", nativeQuery = true)
    void removeRelationship(String id, Integer type);

    @Query(value = "SELECT relationship FROM staff_relationships WHERE staff_id = :AId AND type = :BType AND id = :BId LIMIT 1", nativeQuery = true)
    String getRelationship(String AId, String BId, Integer BType);

    @Modifying
    @Query(value = "UPDATE student SET staff_id = :newId WHERE staff_id = :oldId", nativeQuery = true)
    void updateStaffIdInStudent(String oldId, String newId);

    @Modifying
    @Query(value = "UPDATE teacher SET staff_id = :newId WHERE staff_id = :oldId", nativeQuery = true)
    void updateStaffIdInTeacher(String oldId, String newId);

    @Modifying
    @Query(value = "UPDATE visit_visitors SET visitors_id = :newId WHERE visitors_id = :oldId", nativeQuery = true)
    void updateStaffIdInVisitor(String oldId, String newId);
}
