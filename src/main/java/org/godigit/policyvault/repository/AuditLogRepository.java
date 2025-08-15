package org.godigit.policyvault.repository;

import org.godigit.policyvault.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByPolicyId(UUID policyId);
    List<AuditLog> findByUserId(String userId);
}
