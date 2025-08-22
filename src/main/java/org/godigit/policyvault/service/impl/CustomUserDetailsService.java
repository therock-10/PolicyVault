package org.godigit.policyvault.service.impl;

<<<<<<< HEAD
import org.godigit.policyvault.entities.User;
import org.godigit.policyvault.repository.UserRepository;
import org.godigit.policyvault.security.UserPrincipal;
=======
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.godigit.policyvault.entities.Users;
import org.godigit.policyvault.repository.UserRepository;
>>>>>>> ffbec8c1dffa682dcde92bfc7496e902e3ea8b6c
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
<<<<<<< HEAD
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
=======

    private final UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
>>>>>>> ffbec8c1dffa682dcde92bfc7496e902e3ea8b6c
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
<<<<<<< HEAD
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return UserPrincipal.build(user);
=======
        Users u = null;
        try {
            u = users.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        Set<SimpleGrantedAuthority> authorities = u.getRoles().stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        return new User(
                u.getUsername(), u.getPasswordHash(), u.isEnabled(),
                true, true, true, authorities);
>>>>>>> ffbec8c1dffa682dcde92bfc7496e902e3ea8b6c
    }
}