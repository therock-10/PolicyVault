package org.godigit.policyvault.mapper;

import org.godigit.policyvault.domain.Policy;
import org.godigit.policyvault.dto.PolicyResponse;

public class PolicyMapper {
    public static PolicyResponse toResponse(Policy policy) {
        return new PolicyResponse(
                policy.getId(),
                CurrentVersion(),
                policy.getCreatedAt(),
                policy.getUpdatedAt()
        );
    }
}
