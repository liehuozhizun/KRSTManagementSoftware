package org.krst.app.repositories;

import org.krst.app.domains.PatronSaintDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface PatronSaintDateRepository extends JpaRepository<PatronSaintDate, Integer> {
}
