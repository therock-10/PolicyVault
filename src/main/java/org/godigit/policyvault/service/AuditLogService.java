package org.godigit.policyvault.service;

import org.godigit.policyvault.dto.AuditLogDto;
import org.godigit.policyvault.entities.AuditLog;
import org.godigit.policyvault.entities.Policy;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AuditLogService {
    void log(UUID policyId, String action);
    List<AuditLogDto> getLogsByPolicy(UUID policyId);
    List<AuditLogDto> getLogsByUser(String userId);
}

