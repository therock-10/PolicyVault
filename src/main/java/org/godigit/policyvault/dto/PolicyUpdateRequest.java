package org.godigit.policyvault.dto;

import jakarta.validation.constraints.NotBlank;

public record PolicyUpdateRequest(
        @NotBlank String content,
        @NotBlank String description,
        @NotBlank String changedBy
) {}
