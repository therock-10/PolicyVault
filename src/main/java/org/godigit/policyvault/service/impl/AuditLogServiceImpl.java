package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.entities.AuditLog; import org.godigit.policyvault.entities.Policy; import org.godigit.policyvault.exception.PolicyNotFoundException; import org.godigit.policyvault.exception.UserNotFoundException; import org.godigit.policyvault.repository.AuditLogRepository; import org.godigit.policyvault.repository.PolicyRepository; import org.godigit.policyvault.service.AuditLogService; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.stereotype.Service;

import java.time.Instant; import java.util.List; import java.util.UUID;

@Service public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepo;
    private final PolicyRepository policyRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepo, PolicyRepository policyRepository) {
        this.auditLogRepo = auditLogRepo;
        this.policyRepository = policyRepository;
    }

    @Override
    public void log(String userId, UUID policyId, String action) {
        // Check if the policy exists before logging
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException("Policy with ID " + policyId + " not found"));

        if (userId == null || userId.trim().isEmpty()) {
            throw new UserNotFoundException("User ID cannot be null or empty");
        }

        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setPolicy(policy);
        log.setAction(action);
        auditLogRepo.save(log);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getLogsByPolicy(UUID policyId) {
        // Validate policy existence
        if (!policyRepository.existsById(policyId)) {
            throw new PolicyNotFoundException("Policy with ID " + policyId + " not found");
        }

        return auditLogRepo.findByPolicyId(policyId);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getLogsByUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new UserNotFoundException("User ID cannot be null or empty");
        }

        List<AuditLog> logs = auditLogRepo.findByUserId(userId);
        if (logs.isEmpty()) {
            throw new UserNotFoundException("No logs found for user ID: " + userId);
        }
        return logs;
    }

    @Override
    public void record(String userId, UUID policyId, String action, String description, Instant ts) {

    }
}
