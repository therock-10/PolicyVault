package org.godigit.policyvault.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChangeLogResponse(
        UUID id,
        UUID policyId,
        int oldVersion,
        int newVersion,
        String changedBy,
        String description,
        LocalDateTime changeDate
) {}
