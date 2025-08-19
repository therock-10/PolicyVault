package org.godigit.policyvault.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.godigit.policyvault.entities.Role;
import org.godigit.policyvault.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = null;
        try {
            u = (User) users.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        Set<Role> authorities = u.getRoles().stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        return new User(
                u.getUsername(), u.getPassword(), u.isEnabled(),
                true, true, true, authorities);
    }
}