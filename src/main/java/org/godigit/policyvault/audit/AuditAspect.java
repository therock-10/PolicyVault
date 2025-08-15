package org.godigit.policyvault.audit;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.godigit.policyvault.service.AuditLogService;

import java.util.UUID;

@Aspect
@Component
public class AuditAspect {

    private final AuditLogService auditLogService;

    public AuditAspect(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @AfterReturning(pointcut = "execution(* org.godigit.policyvault.service.PolicyService.getPolicy(..)) && args(id)", returning = "result")
    public void logView(UUID id, Object result) {
        auditLogService.log("system-user", id, "VIEW");
    }

    @AfterReturning(pointcut = "execution(* org.godigit.policyvault.service.PolicyService.updatePolicy(..)) && args(id,..)")
    public void logUpdate(UUID id) {
        auditLogService.log("system-user", id, "UPDATE");
    }

    @AfterReturning(pointcut = "execution(* org.godigit.policyvault.service.PolicyService.deletePolicy(..)) && args(id)")
    public void logDelete(UUID id) {
        auditLogService.log("system-user", id, "DELETE");
    }
}
