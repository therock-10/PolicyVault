package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.dto.*;
import org.godigit.policyvault.entities.*;
import org.godigit.policyvault.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PolicyServiceImplTest {

    private PolicyRepository policyRepo;
    private PolicyVersionRepository versionRepo;
    private ChangeLogRepository changeLogRepo;
    private PolicyServiceImpl policyService;

    @BeforeEach
    void setUp() {
        policyRepo = mock(PolicyRepository.class);
        versionRepo = mock(PolicyVersionRepository.class);
        changeLogRepo = mock(ChangeLogRepository.class);
        policyService = new PolicyServiceImpl(policyRepo, versionRepo, changeLogRepo);
    }

    @Test
    void testCreatePolicy_ShouldSavePolicyAndVersion() {
        PolicyCreateRequest request = new PolicyCreateRequest("Title", "HR", "Content");

        UUID generatedId = UUID.randomUUID();
        Policy policy = new Policy();
        policy.setId(generatedId);

        when(policyRepo.save(any(Policy.class))).thenAnswer(invocation -> {
            Policy p = invocation.getArgument(0);
            p.setId(generatedId);
            return p;
        });

        UUID resultId = policyService.createPolicy(request);

        assertEquals(generatedId, resultId);
        verify(policyRepo).save(any(Policy.class));
        verify(versionRepo).save(any(PolicyVersion.class));
    }

    @Test
    void testGetPolicy_ShouldReturnPolicyResponse() {
        UUID policyId = UUID.randomUUID();
        Policy policy = new Policy();
        policy.setId(policyId);
        policy.setTitle("Title");
        policy.setDepartment("HR");
        policy.setCurrentVersion(1);
        policy.setCreatedAt(LocalDateTime.now());
        policy.setUpdatedAt(LocalDateTime.now());

        when(policyRepo.findById(policyId)).thenReturn(Optional.of(policy));

        PolicyResponse response = policyService.getPolicy(policyId);

        assertEquals(policyId, response.id());
        assertEquals("Title", response.title());
        verify(policyRepo).findById(policyId);
    }

    @Test
    void testUpdatePolicy_ShouldSaveNewVersionAndChangeLog() {
        UUID policyId = UUID.randomUUID();
        Policy policy = new Policy();
        policy.setId(policyId);
        policy.setCurrentVersion(1);

        PolicyUpdateRequest request = new PolicyUpdateRequest("Updated content", "Updated desc", "riket");

        when(policyRepo.findById(policyId)).thenReturn(Optional.of(policy));

        policyService.updatePolicy(policyId, request);

        verify(versionRepo).save(any(PolicyVersion.class));
        verify(changeLogRepo).save(any(ChangeLog.class));
        verify(policyRepo).save(policy);
    }

    @Test
    void testDeletePolicy_ShouldCallRepositoryDelete() {
        UUID policyId = UUID.randomUUID();

        policyService.deletePolicy(policyId);

        verify(policyRepo).deleteById(policyId);
    }

    @Test
    void testSearchPolicies_ShouldFilterAndMapResults() {
        Policy p1 = new Policy();
        p1.setId(UUID.randomUUID());
        p1.setTitle("HR Policy");
        p1.setDepartment("HR");
        p1.setCurrentVersion(1);
        p1.setCreatedAt(LocalDateTime.now());
        p1.setUpdatedAt(LocalDateTime.now());

        Policy p2 = new Policy();
        p2.setId(UUID.randomUUID());
        p2.setTitle("Finance Policy");
        p2.setDepartment("Finance");
        p2.setCurrentVersion(1);
        p2.setCreatedAt(LocalDateTime.now());
        p2.setUpdatedAt(LocalDateTime.now());

        when(policyRepo.findAll()).thenReturn(List.of(p1, p2));

        List<PolicyResponse> results = policyService.searchPolicies("HR", "Policy");

        assertEquals(1, results.size());
        assertEquals("HR Policy", results.get(0).title());
        verify(policyRepo).findAll();
    }
}
