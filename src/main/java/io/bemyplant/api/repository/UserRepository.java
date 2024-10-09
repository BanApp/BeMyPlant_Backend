package io.bemyplant.api.repository;


import io.bemyplant.api.entity.UserInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<UserInfo> findOneWithAuthoritiesByUsername(String username);
}