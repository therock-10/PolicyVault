package org.godigit.policyvault.service;

<<<<<<< HEAD
import org.godigit.policyvault.entities.User;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);

=======
import org.godigit.policyvault.entities.Users;
import org.godigit.policyvault.entities.Role;

import java.util.Set;

public interface UserService {
    Users createUser(String username, String email, String rawPassword, String department, Set<Role> roles);
    void touchLogin(String username);
>>>>>>> ffbec8c1dffa682dcde92bfc7496e902e3ea8b6c
}