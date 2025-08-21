package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.dto.ChangeLogResponse;
import org.godigit.policyvault.entities.ChangeLog;
import org.godigit.policyvault.entities.Policy;
import org.godigit.policyvault.repository.ChangeLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ChangeLogServiceImplTest {

    private ChangeLogRepository changeLogRepo;
    private ChangeLogServiceImpl changeLogService;

    @BeforeEach
    void setUp() {
        changeLogRepo = mock(ChangeLogRepository.class);
        changeLogService = new ChangeLogServiceImpl(changeLogRepo);
    }

    @Test
    void testGetChangeLogs_ShouldReturnMappedDTOs() {
        UUID policyId = UUID.randomUUID();
        Policy policy = new Policy();
        policy.setId(policyId);

        ChangeLog log1 = new ChangeLog();
        log1.setId(UUID.randomUUID());
        log1.setPolicy(policy);
        log1.setOldVersion(1);
        log1.setNewVersion(2);
        log1.setChangedBy("user1");
        log1.setDescription("Initial change");
        log1.setChangeDate(LocalDateTime.now());

        ChangeLog log2 = new ChangeLog();
        log2.setId(UUID.randomUUID());
        log2.setPolicy(policy);
        log2.setOldVersion(2);
        log2.setNewVersion(3);
        log2.setChangedBy("user2");
        log2.setDescription("Second change");
        log2.setChangeDate(LocalDateTime.now());

        when(changeLogRepo.findByPolicyId(policyId)).thenReturn(List.of(log1, log2));

        List<ChangeLogResponse> result = changeLogService.getChangeLogs(policyId);

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).changedBy());
        assertEquals(3, result.get(1).newVersion());

        verify(changeLogRepo).findByPolicyId(policyId);
    }
}
