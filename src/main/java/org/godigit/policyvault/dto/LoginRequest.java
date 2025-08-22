package org.godigit.policyvault.dto;

public record LoginRequest(
        String username,
        String password
) {}
