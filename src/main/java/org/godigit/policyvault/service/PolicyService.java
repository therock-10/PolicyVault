package org.godigit.policyvault.service;

import org.godigit.policyvault.dto.*;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import org.springframework.security.access.prepost.PreAuthorize;
=======

>>>>>>> b04c446cccbcdb6b0f28d11cd37acf12fe01498d
import java.util.List;
import java.util.UUID;

@Service
public interface PolicyService {
    UUID createPolicy(PolicyCreateRequest request);
    PolicyResponse getPolicy(UUID id);
    void updatePolicy(UUID id, PolicyUpdateRequest request);
    void deletePolicy(UUID id);
    List<PolicyResponse> searchPolicies(String department, String keyword);
}
