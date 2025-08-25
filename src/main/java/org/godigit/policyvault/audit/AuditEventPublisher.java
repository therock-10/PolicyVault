package org.godigit.policyvault.audit;

import org.godigit.policyvault.service.AuditLogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class AuditEventPublisher {

    private final AuditLogService auditLogService;

    public AuditEventPublisher(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    public void publish(String action, UUID policyId, String description) {
        String userId = resolveUserId();
        Instant ts = Instant.now();
        //auditLogService.record(userId, policyId, action, description, ts);
    }

    private String resolveUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return "system";
        Object principal = auth.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails ud) {
            return ud.getUsername();
        }
        return auth.getName() != null ? auth.getName() : "unknown";
    }
}