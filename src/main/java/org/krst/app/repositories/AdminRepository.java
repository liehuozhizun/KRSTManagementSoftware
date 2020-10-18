package org.krst.app.repositories;

import org.krst.app.domains.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Login, Integer> {
}
