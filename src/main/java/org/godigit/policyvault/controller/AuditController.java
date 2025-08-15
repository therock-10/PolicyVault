package org.godigit.policyvault.controller;

import org.godigit.policyvault.domain.AuditLog;
import org.godigit.policyvault.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditLogService auditLogService;

    public AuditController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<AuditLog>> getLogsByPolicy(@PathVariable UUID policyId) {
        return ResponseEntity.ok(auditLogService.getLogsByPolicy(policyId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLog>> getLogsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(auditLogService.getLogsByUser(userId));
    }
}
