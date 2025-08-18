package org.godigit.policyvault.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditLogDto(
    UUID id,
    String userId,
    UUID policyId,
    String action,
    LocalDateTime timestamp
){}


