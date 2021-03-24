package org.krst.app.repositories;

import org.krst.app.domains.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AttributeRepository extends JpaRepository<Attribute, String> {
    @Modifying
    @Query(value = "UPDATE student SET attribute_attribute = :newAttribute WHERE attribute_attribute = :oldAttribute", nativeQuery = true)
    void updateAttributeInStudent(String oldAttribute, String newAttribute);

    @Modifying
    @Query(value = "UPDATE teacher SET attribute_attribute = :newAttribute WHERE attribute_attribute = :oldAttribute", nativeQuery = true)
    void updateAttributeInTeacher(String oldAttribute, String newAttribute);
}
