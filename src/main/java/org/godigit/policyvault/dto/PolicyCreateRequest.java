package org.godigit.policyvault.dto;

import jakarta.validation.constraints.NotBlank;

public record PolicyCreateRequest(
        @NotBlank String title,
        @NotBlank String department,
        @NotBlank String content
) {}
