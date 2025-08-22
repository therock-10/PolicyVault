package org.godigit.policyvault.controller;

import org.godigit.policyvault.entities.AuditLog;
import org.godigit.policyvault.entities.Policy;
import org.godigit.policyvault.service.AuditLogService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuditController.class)
public class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuditLogService auditLogService;

    @Test
    void testGetLogsByPolicy() throws Exception {
        UUID policyId = UUID.randomUUID();

        Policy policy = new Policy();
        policy.setId(policyId);
        policy.setTitle("Data Protection Policy");
        policy.setDepartment("IT");
        policy.setCurrentVersion(1);

        AuditLog log = new AuditLog();
        log.setId(UUID.randomUUID());
        log.setUserId("user123");
        log.setPolicy(policy);
        log.setAction("CREATED");
        log.setTimestamp(LocalDateTime.now());

        Mockito.when(auditLogService.getLogsByPolicy(policyId)).thenReturn(List.of(log));

        mockMvc.perform(get("/audit/policy/" + policyId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId").value("user123"))
                .andExpect(jsonPath("$[0].action").value("CREATED"));
    }

    @Test
    void testGetLogsByUser() throws Exception {
        String userId = "user123";

        Policy policy = new Policy();
        policy.setId(UUID.randomUUID());
        policy.setTitle("Security Policy");
        policy.setDepartment("Security");
        policy.setCurrentVersion(2);

        AuditLog log = new AuditLog();
        log.setId(UUID.randomUUID());
        log.setUserId(userId);
        log.setPolicy(policy);
        log.setAction("UPDATED");
        log.setTimestamp(LocalDateTime.now());

        Mockito.when(auditLogService.getLogsByUser(userId)).thenReturn(List.of(log));

        mockMvc.perform(get("/audit/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId").value("user123"))
                .andExpect(jsonPath("$[0].action").value("UPDATED"));
    }
}
