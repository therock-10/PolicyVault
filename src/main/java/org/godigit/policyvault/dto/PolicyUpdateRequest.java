package org.godigit.policyvault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record PolicyUpdateRequest(
        @NotNull MultipartFile file,
        @NotBlank String description,
        @NotBlank String changedBy
) {}
