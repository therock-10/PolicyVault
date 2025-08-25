package org.godigit.policyvault.service;

import org.godigit.policyvault.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PolicyService {
    UUID createPolicy(PolicyCreateRequest request) throws Exception;
    PolicyResponse getPolicy(UUID id);
    void updatePolicy(UUID id, PolicyUpdateRequest request) throws Exception;
    void deletePolicy(UUID id);
    List<PolicyResponse> searchPolicies(String department, String keyword);
}
