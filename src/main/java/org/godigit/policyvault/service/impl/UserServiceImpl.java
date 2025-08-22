package org.godigit.policyvault.service.impl;

<<<<<<< HEAD
import org.godigit.policyvault.entities.User;
import org.godigit.policyvault.repository.UserRepository;
import org.godigit.policyvault.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
=======
import org.godigit.policyvault.entities.Role; import org.godigit.policyvault.entities.Users; import org.godigit.policyvault.repository.UserRepository; import org.godigit.policyvault.service.UserService; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;

import java.time.Instant; import java.util.Set;

@Service public class UserServiceImpl implements UserService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    @Transactional
   //@PreAuthorize("hasRole('ADMIN')")
    public Users createUser(String username, String email, String rawPassword,
                            String department, Set<Role> roles) {
        Users u = new Users();
        u.setUsername(username);
        u.setEmail(email);
        u.setDepartment(department);
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setRoles(roles);
        return users.save(u);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN','COMPLIANCE_OFFICER','DEPARTMENT_HEAD','EMPLOYEE')")
    public void touchLogin(String username) {
        users.findByUsername(username).ifPresent(u -> {
            u.setLastLoginAt(Instant.now());
            users.save(u);
        });
>>>>>>> ffbec8c1dffa682dcde92bfc7496e902e3ea8b6c
    }
}