package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.dto.ChangeLogResponse;
import org.godigit.policyvault.entities.ChangeLog;
import org.godigit.policyvault.exception.PolicyNotFoundException;
import org.godigit.policyvault.repository.ChangeLogRepository;
import org.godigit.policyvault.repository.PolicyRepository;
import org.godigit.policyvault.service.ChangeLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List; import java.util.UUID;

@Service public class ChangeLogServiceImpl implements ChangeLogService {

    private final ChangeLogRepository changeLogRepo;
    private final PolicyRepository policyRepository;

    public ChangeLogServiceImpl(ChangeLogRepository changeLogRepo, PolicyRepository policyRepository) {
        this.changeLogRepo = changeLogRepo;
        this.policyRepository = policyRepository;
    }

    @Override
    @PreAuthorize("hasAnyRole('COMPLIANCE_OFFICER','ADMIN')")
    public List<ChangeLogResponse> getChangeLogs(UUID policyId) {
        // Validate policy existence
        if (!policyRepository.existsById(policyId)) {
            throw new PolicyNotFoundException("Policy with ID " + policyId + " not found");
        }

        // Fetch logs
        List<ChangeLog> logs = changeLogRepo.findByPolicyId(policyId);
        if (logs.isEmpty()) {
            throw new PolicyNotFoundException("No change logs found for policy ID: " + policyId);
        }

        // Convert entity to DTO
        return logs.stream()
                .map(cl -> new ChangeLogResponse(
                        cl.getId(),
                        cl.getPolicy().getId(),
                        cl.getOldVersion(),
                        cl.getNewVersion(),
                        cl.getChangedBy(),
                        cl.getDescription(),
                        cl.getChangeDate()
                ))
                .toList();
    }
}
