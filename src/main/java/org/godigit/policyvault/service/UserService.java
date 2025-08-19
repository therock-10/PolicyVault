package org.godigit.policyvault.service;

import java.time.Instant;
import java.util.Set;
import org.godigit.policyvault.entities.Role;
import org.godigit.policyvault.entities.User;
import org.godigit.policyvault.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Transactional
    public User createUser(String username, String email, String rawPassword,
                           String department, Set<Role> roles) {
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setDepartment(department);
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setRoles(roles);
        return users.save(u);
    }

    @Transactional
    public void touchLogin(String username) {
        users.findByUsername(username).ifPresent((User) u -> {
            u.setLastLoginAt(Instant.now());
            users.save(u);
        });
    }
}