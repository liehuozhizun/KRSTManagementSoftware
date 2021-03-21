package org.krst.app.repositories;

import org.krst.app.domains.CourseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CourseTemplateRepository extends JpaRepository<CourseTemplate, String> {
    @Modifying
    @Query(value = "UPDATE course SET course_template_id = :newId WHERE course_template_id = :oldId", nativeQuery = true)
    void updateTemplateIdInCourse(String oldId, String newId);
}
