package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.dto.ChangeLogResponse;
import org.godigit.policyvault.repository.ChangeLogRepository;
import org.godigit.policyvault.service.ChangeLogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChangeLogServiceImpl implements ChangeLogService {

    private final ChangeLogRepository changeLogRepo;

    public ChangeLogServiceImpl(ChangeLogRepository changeLogRepo) {
        this.changeLogRepo = changeLogRepo;
    }

    @Override
    public List<ChangeLogResponse> getChangeLogs(UUID policyId) {
        return changeLogRepo.findByPolicyId(policyId).stream()
                .map(cl -> new ChangeLogResponse(cl.getId(), cl.getPolicyId(), cl.getOldVersion(),
                        cl.getNewVersion(), cl.getChangedBy(), cl.getDescription(), cl.getChangeDate()))
                .toList();
    }
}
