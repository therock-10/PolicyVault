package org.godigit.policyvault.service.impl;

import java.util.Set;
import java.util.stream.Collectors;
import org.godigit.policyvault.entities.Users;
import org.godigit.policyvault.exception.UserNotFoundException;
import org.godigit.policyvault.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

    public UserDetails loadUserByEmail(String email){
        Users u = users.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email'" + email + "' not found"));

        Set<SimpleGrantedAuthority> authorities = u.getRoles().stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new User(
                u.getUsername(),
                u.getPasswordHash(),
                u.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return loadUserByEmail(username);
    }
}
