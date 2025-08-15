package org.godigit.policyvault.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PolicyVersionResponse(
        UUID id,
        UUID policyId,
        int version,
        String content,
        LocalDateTime createdAt
) {}
