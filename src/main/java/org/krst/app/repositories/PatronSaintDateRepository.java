package org.krst.app.repositories;

import org.krst.app.domains.PatronSaintDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronSaintDateRepository extends JpaRepository<PatronSaintDate, Integer> {
}
