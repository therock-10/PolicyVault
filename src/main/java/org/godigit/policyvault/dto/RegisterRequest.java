package org.godigit.policyvault.dto;

import org.godigit.policyvault.entities.Role;
import java.util.Set;

public record RegisterRequest(
        String username,
        String email,
        String password,
        String department,
        Set<Role> roles
) {}
