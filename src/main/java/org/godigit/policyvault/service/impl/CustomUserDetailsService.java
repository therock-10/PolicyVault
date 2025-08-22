package org.godigit.policyvault.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.godigit.policyvault.entities.Users;
import org.godigit.policyvault.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Users u = null;
        try {
            u = users.findByEmail(email)
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
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return loadUserByEmail(email);
    }
}