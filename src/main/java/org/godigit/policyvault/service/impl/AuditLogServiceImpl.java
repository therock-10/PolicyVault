package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.domain.AuditLog;
import org.godigit.policyvault.repository.AuditLogRepository;
import org.godigit.policyvault.service.AuditLogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepo;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepo) {
        this.auditLogRepo = auditLogRepo;
    }

    @Override
    public void log(String userId, UUID policyId, String action) {
        var log = new AuditLog();
        log.setUserId(userId);
        log.setPolicyId(policyId);
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
}
