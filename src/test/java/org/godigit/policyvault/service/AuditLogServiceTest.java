package org.godigit.policyvault.service;

import org.godigit.policyvault.entities.AuditLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuditLogServiceTest {

    private AuditLogService auditLogService;

    @BeforeEach
    void setUp() {
        auditLogService = mock(AuditLogService.class);
    }

    @Test
    void testLogMethod() {
        String userId = "user123";
        UUID policyId = UUID.randomUUID();
        String action = "VIEW";

        doNothing().when(auditLogService).log(userId, policyId, action);

        auditLogService.log(userId, policyId, action);

        verify(auditLogService, times(1)).log(userId, policyId, action);
    }

    @Test
    void testGetLogsByPolicy() {
        UUID policyId = UUID.randomUUID();
        List<AuditLog> mockLogs = List.of(new AuditLog(), new AuditLog());

        when(auditLogService.getLogsByPolicy(policyId)).thenReturn(mockLogs);

        List<AuditLog> result = auditLogService.getLogsByPolicy(policyId);

        assertEquals(2, result.size());
        verify(auditLogService).getLogsByPolicy(policyId);
    }

    @Test
    void testGetLogsByUser() {
        String userId = "user123";
        List<AuditLog> mockLogs = List.of(new AuditLog());

        when(auditLogService.getLogsByUser(userId)).thenReturn(mockLogs);

        List<AuditLog> result = auditLogService.getLogsByUser(userId);

        assertEquals(1, result.size());
        verify(auditLogService).getLogsByUser(userId);
    }

    @Test
    void testRecordMethod() {
        String userId = "user123";
        UUID policyId = UUID.randomUUID();
        String action = "UPDATE";
        String description = "Policy updated";
        Instant timestamp = Instant.now();

        doNothing().when(auditLogService).record(userId, policyId, action, description, timestamp);

        auditLogService.record(userId, policyId, action, description, timestamp);

        verify(auditLogService).record(userId, policyId, action, description, timestamp);
    }
}
