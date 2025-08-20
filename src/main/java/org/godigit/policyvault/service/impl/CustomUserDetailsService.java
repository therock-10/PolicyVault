package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.entities.User;
import org.godigit.policyvault.repository.UserRepository;
import org.godigit.policyvault.security.UserPrincipal;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return UserPrincipal.build(user);
    }
}