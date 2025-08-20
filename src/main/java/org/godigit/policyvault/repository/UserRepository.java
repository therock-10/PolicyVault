package org.godigit.policyvault.repository;

import java.util.Optional;
import org.godigit.policyvault.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository<User> extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}