package org.godigit.policyvault.dto;

import org.godigit.policyvault.entities.Role;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UsersDto(
        UUID id,
        String username,
        String email,
        String department,
        Set<Role> roles,
        Instant createdAt,
        Instant lastLoginAt,
        boolean enabled
) {}
