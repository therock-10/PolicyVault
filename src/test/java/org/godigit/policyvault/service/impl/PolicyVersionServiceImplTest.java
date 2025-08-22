package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.dto.PolicyVersionResponse;
import org.godigit.policyvault.entities.Policy;
import org.godigit.policyvault.entities.PolicyVersion;
import org.godigit.policyvault.repository.PolicyVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PolicyVersionServiceImplTest {

    private PolicyVersionRepository versionRepo;
    private PolicyVersionServiceImpl versionService;

    @BeforeEach
    void setUp() {
        versionRepo = mock(PolicyVersionRepository.class);
        versionService = new PolicyVersionServiceImpl(versionRepo);
    }

    @Test
    void testGetAllVersions_ShouldReturnMappedResponses() {
        UUID policyId = UUID.randomUUID();
        Policy policy = new Policy();
        policy.setId(policyId);

        PolicyVersion v1 = new PolicyVersion();
        v1.setId(UUID.randomUUID());
        v1.setPolicy(policy);
        v1.setVersion(2);
        v1.setContent("Second version");
        v1.setCreatedAt(LocalDateTime.now());

        PolicyVersion v2 = new PolicyVersion();
        v2.setId(UUID.randomUUID());
        v2.setPolicy(policy);
        v2.setVersion(1);
        v2.setContent("First version");
        v2.setCreatedAt(LocalDateTime.now().minusDays(1));

        when(versionRepo.findByPolicyIdOrderByVersionDesc(policyId)).thenReturn(List.of(v1, v2));

        List<PolicyVersionResponse> responses = versionService.getAllVersions(policyId);

        assertEquals(2, responses.size());
        assertEquals(2, responses.get(0).version());
        assertEquals("Second version", responses.get(0).content());
        verify(versionRepo).findByPolicyIdOrderByVersionDesc(policyId);
    }

    @Test
    void testGetVersion_ShouldReturnMappedResponse() {
        UUID policyId = UUID.randomUUID();
        Policy policy = new Policy();
        policy.setId(policyId);

        PolicyVersion version = new PolicyVersion();
        version.setId(UUID.randomUUID());
        version.setPolicy(policy);
        version.setVersion(1);
        version.setContent("Initial content");
        version.setCreatedAt(LocalDateTime.now());

        when(versionRepo.findByPolicyIdAndVersion(policyId, 1)).thenReturn(version);

        PolicyVersionResponse response = versionService.getVersion(policyId, 1);

        assertEquals(1, response.version());
        assertEquals("Initial content", response.content());
        verify(versionRepo).findByPolicyIdAndVersion(policyId, 1);
    }
}
