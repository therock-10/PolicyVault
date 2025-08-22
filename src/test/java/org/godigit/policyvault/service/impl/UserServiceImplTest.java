package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.entities.Role;
import org.godigit.policyvault.entities.Users;
import org.godigit.policyvault.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void testCreateUser_ShouldEncodePasswordAndSaveUser() {
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Users user = userService.createUser("riket", "riket@example.com", rawPassword, "HR", Set.of(Role.ROLE_ADMIN));

        assertEquals("riket", user.getUsername());
        assertEquals("riket@example.com", user.getEmail());
        assertEquals("HR", user.getDepartment());
        assertEquals(encodedPassword, user.getPasswordHash());
        assertTrue(user.getRoles().contains(Role.ROLE_ADMIN));

        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(any(Users.class));
    }

    @Test
    void testTouchLogin_ShouldUpdateLastLoginIfUserExists() {
        Users user = new Users();
        user.setUsername("riket");

        when(userRepository.findByUsername("riket")).thenReturn(Optional.of(user));
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.touchLogin("riket");

        assertNotNull(user.getLastLoginAt());
        assertTrue(user.getLastLoginAt().isBefore(Instant.now().plusSeconds(1)));

        verify(userRepository).findByUsername("riket");
        verify(userRepository).save(user);
    }

    @Test
    void testTouchLogin_ShouldDoNothingIfUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        userService.touchLogin("unknown");

        verify(userRepository).findByUsername("unknown");
        verify(userRepository, never()).save(any());
    }
}
