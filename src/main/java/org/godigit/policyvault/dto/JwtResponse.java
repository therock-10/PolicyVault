package org.godigit.policyvault.dto;

import java.util.Set;

public record JwtResponse(
        String token,
        String email,
        Set<String> roles
) {}
