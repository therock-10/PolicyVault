package org.godigit.policyvault.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record PolicyCreateRequest(
        @NotBlank String title,
        @NotBlank String department,
        @NotBlank MultipartFile file
        ) {}
