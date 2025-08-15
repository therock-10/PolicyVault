package org.godigit.policyvault.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PolicyResponse(
        UUID id,
        String title,
        String department,
        int currentVersion,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
