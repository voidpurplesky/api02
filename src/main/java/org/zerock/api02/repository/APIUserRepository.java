package org.zerock.api02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.api02.domain.APIUser;

public interface APIUserRepository extends JpaRepository<APIUser, String> {
}
