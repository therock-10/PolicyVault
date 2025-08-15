package org.godigit.policyvault.service;

import org.godigit.policyvault.dto.PolicyVersionResponse;

import java.util.List;
import java.util.UUID;

public interface PolicyVersionService {
    List<PolicyVersionResponse> getAllVersions(UUID policyId);
    PolicyVersionResponse getVersion(UUID policyId, int version);
}
