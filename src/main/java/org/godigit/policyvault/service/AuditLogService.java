package org.godigit.policyvault.service;

import org.godigit.policyvault.domain.AuditLog;

import java.util.List;
import java.util.UUID;

public interface AuditLogService {
    void log(String userId, UUID policyId, String action);
    List<AuditLog> getLogsByPolicy(UUID policyId);
    List<AuditLog> getLogsByUser(String userId);
}
