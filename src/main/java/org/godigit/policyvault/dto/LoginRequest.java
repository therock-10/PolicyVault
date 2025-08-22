package org.godigit.policyvault.dto;

public record LoginRequest(
        String email,
        String password
) {}
