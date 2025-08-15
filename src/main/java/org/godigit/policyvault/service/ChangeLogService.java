package org.godigit.policyvault.service;

import org.godigit.policyvault.dto.ChangeLogResponse;

import java.util.List;
import java.util.UUID;

public interface ChangeLogService {
    List<ChangeLogResponse> getChangeLogs(UUID policyId);
}
