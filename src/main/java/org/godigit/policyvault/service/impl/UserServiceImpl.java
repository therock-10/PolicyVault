package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.entities.Role;
import org.godigit.policyvault.entities.Users;
import org.godigit.policyvault.repository.UserRepository;
import org.godigit.policyvault.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public Users createUser(String username, String email, String rawPassword,
                            String department, Set<Role> roles) {
        Users u = new Users();
        u.setUsername(username);
        u.setEmail(email);
        u.setDepartment(department);
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setRoles(roles);
        return (Users) users.save(u);
    }


    @Override
    @Transactional
    public void touchLogin(String username) {
        users.findByUsername(username).ifPresent(u -> {
            u.setLastLoginAt(Instant.now());
            users.save(u);
        });

    }
}