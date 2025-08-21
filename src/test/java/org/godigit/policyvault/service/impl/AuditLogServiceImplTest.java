package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.entities.AuditLog;
import org.godigit.policyvault.entities.Policy;
import org.godigit.policyvault.repository.AuditLogRepository;
import org.godigit.policyvault.repository.PolicyRepository;
import org.godigit.policyvault.service.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuditLogServiceImplTest {

    private AuditLogRepository auditLogRepo;
    private PolicyRepository policyRepository;
    private AuditLogService auditLogService;

    @BeforeEach
    void setUp() {
        auditLogRepo = mock(AuditLogRepository.class);
        policyRepository = mock(PolicyRepository.class);
        auditLogService = new AuditLogServiceImpl(auditLogRepo, policyRepository);
    }

    @Test
    void testLog_ShouldSaveAuditLog() {
        String userId = "user123";
        UUID policyId = UUID.randomUUID();
        String action = "VIEW";
        Policy mockPolicy = new Policy();

        when(policyRepository.getReferenceById(policyId)).thenReturn(mockPolicy);

        auditLogService.log(userId, policyId, action);

        verify(policyRepository).getReferenceById(policyId);
        verify(auditLogRepo).save(any(AuditLog.class));
    }

    @Test
    void testGetLogsByPolicy_ShouldReturnLogs() {
        UUID policyId = UUID.randomUUID();
        List<AuditLog> logs = List.of(new AuditLog(), new AuditLog());

        when(auditLogRepo.findByPolicyId(policyId)).thenReturn(logs);

        List<AuditLog> result = auditLogService.getLogsByPolicy(policyId);

        assertEquals(2, result.size());
        verify(auditLogRepo).findByPolicyId(policyId);
    }

    @Test
    void testGetLogsByUser_ShouldReturnLogs() {
        String userId = "user123";
        List<AuditLog> logs = List.of(new AuditLog());

        when(auditLogRepo.findByUserId(userId)).thenReturn(logs);

        List<AuditLog> result = auditLogService.getLogsByUser(userId);

        assertEquals(1, result.size());
        verify(auditLogRepo).findByUserId(userId);
    }

    @Test
    void testRecord_ShouldNotThrow() {
        assertDoesNotThrow(() -> auditLogService.record(
                "user123",
                UUID.randomUUID(),
                "UPDATE",
                "Updated policy",
                Instant.now()
        ));
    }
}
