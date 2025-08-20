package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.entities.AuditLog;
import org.godigit.policyvault.entities.Policy;
import org.godigit.policyvault.repository.AuditLogRepository;
import org.godigit.policyvault.repository.PolicyRepository;
import org.godigit.policyvault.service.AuditLogService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepo;
    private final PolicyRepository policyRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepo, PolicyRepository policyRepository) {
        this.auditLogRepo = auditLogRepo;
        this.policyRepository = policyRepository;
    }

    @Override
    public void log(String userId, UUID policyId, String action) {
        var log = new AuditLog();
        log.setUserId(userId);
        log.setPolicy(policyRepository.getReferenceById(policyId));
        log.setAction(action);
        auditLogRepo.save(log);
    }

    @Override
    public List<AuditLog> getLogsByPolicy(UUID policyId) {
        return auditLogRepo.findByPolicyId(policyId);
    }

    @Override
    public List<AuditLog> getLogsByUser(String userId) {
        return auditLogRepo.findByUserId(userId);
    }

    @Override
    public void record(String userId, UUID policyId, String action, String description, Instant ts) {

    }
}
