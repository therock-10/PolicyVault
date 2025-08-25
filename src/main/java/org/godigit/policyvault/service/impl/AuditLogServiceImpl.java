package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.dto.AuditLogDto;
import org.godigit.policyvault.entities.AuditLog; import org.godigit.policyvault.entities.Policy; import org.godigit.policyvault.exception.PolicyNotFoundException; import org.godigit.policyvault.exception.UserNotFoundException;
import org.godigit.policyvault.mapper.AuditLogMapper;
import org.godigit.policyvault.repository.AuditLogRepository; import org.godigit.policyvault.repository.PolicyRepository;
import org.godigit.policyvault.security.JwtService;
import org.godigit.policyvault.service.AuditLogService;
import org.godigit.policyvault.util.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.stereotype.Service;

import java.time.Instant; import java.util.List; import java.util.UUID;

@Service public class
AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepo;
    private final PolicyRepository policyRepository;
    private final JwtService jwtService;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepo, PolicyRepository policyRepository, JwtService jwtService) {
        this.auditLogRepo = auditLogRepo;
        this.policyRepository = policyRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void log(UUID policyId, String action) {

        // Check if the policy exists before logging
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new PolicyNotFoundException("Policy with ID " + policyId + " not found"));

        String userId = SecurityUtils.getCurrentUserId(jwtService);

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
    public List<AuditLogDto> getLogsByPolicy(UUID policyId) {
        // Validate policy existence
        if (!policyRepository.existsById(policyId)) {
            throw new PolicyNotFoundException("Policy with ID " + policyId + " not found");
        }

        List<AuditLog> logs = auditLogRepo.findByPolicyId(policyId);
        return logs.stream()
                .map(this::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLogDto> getLogsByUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new UserNotFoundException("User ID cannot be null or empty");
        }

        List<AuditLog> logs = auditLogRepo.findByUserId(userId);
        if (logs.isEmpty()) {
            throw new UserNotFoundException("No logs found for user ID: " + userId);
        }
        return logs.stream()
                .map(this::toDto)
                .toList();
    }

    private AuditLogDto toDto(AuditLog log) {
        return new AuditLogDto(
                log.getId(),
                log.getUserId(),
                log.getPolicy().getId(),
                log.getAction(),
                log.getTimestamp()
        );
    }

}
