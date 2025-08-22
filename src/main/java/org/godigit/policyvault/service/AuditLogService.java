package org.godigit.policyvault.service;

import org.godigit.policyvault.entities.AuditLog;
import org.godigit.policyvault.entities.Policy;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AuditLogService {
    void log(String userId, UUID policyId, String action);
    List<AuditLog> getLogsByPolicy(UUID policyId);
    List<AuditLog> getLogsByUser(String userId);

    void record(String userId, UUID policyId, String action, String description, Instant ts);
}
