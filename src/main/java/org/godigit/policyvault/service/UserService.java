package org.godigit.policyvault.service;

import org.godigit.policyvault.entities.User;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);

}