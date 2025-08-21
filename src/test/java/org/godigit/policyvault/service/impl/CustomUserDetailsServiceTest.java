package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.entities.Role;
import org.godigit.policyvault.entities.Users;
import org.godigit.policyvault.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.*;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private UserRepository userRepository;
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void testLoadUserByUsername_UserFound_ShouldReturnUserDetails() {
        Users user = new Users();
        user.setId(UUID.randomUUID());
        user.setUsername("riket");
        user.setPasswordHash("secureHash");
        user.setEmail("riket@example.com");
        user.setEnabled(true);
        user.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_EMPLOYEE));

        when(userRepository.findByUsername("riket")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("riket");

        assertEquals("riket", result.getUsername());
        assertEquals("secureHash", result.getPassword());
        assertTrue(result.isEnabled());

        Set<String> authorities = result.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toSet());

        assertTrue(authorities.contains("ROLE_ADMIN"));
        assertTrue(authorities.contains("ROLE_EMPLOYEE"));

        verify(userRepository).findByUsername("riket");
    }

    @Test
    void testLoadUserByUsername_UserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknown");
        });

        verify(userRepository).findByUsername("unknown");
    }
}
