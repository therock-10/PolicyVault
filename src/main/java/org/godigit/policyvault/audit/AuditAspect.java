package org.godigit.policyvault.audit;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
public class AuditAspect {

    private static final Pattern UUID_IN_PATH = Pattern.compile(
            "([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12})"
    );

    private final HttpServletRequest request;
    private final AuditEventPublisher publisher;

    public AuditAspect(HttpServletRequest request, AuditEventPublisher publisher) {
        this.request = request;
        this.publisher = publisher;
    }

    @AfterReturning("@annotation(getMapping) && within(org.godigit.policyvault.controller..*)")
    public void afterGet(JoinPoint jp, GetMapping getMapping) {
        if (!isPoliciesEndpoint()) return;
        publish("VIEW");
    }

    @AfterReturning("@annotation(putMapping) && within(org.godigit.policyvault.controller..*)")
    public void afterPut(JoinPoint jp, PutMapping putMapping) {
        if (!isPoliciesEndpoint()) return;
        publish("UPDATE");
    }

    @AfterReturning("@annotation(postMapping) && within(org.godigit.policyvault.controller..*)")
    public void afterPost(JoinPoint jp, PostMapping postMapping) {
        if (!isPoliciesEndpoint()) return;
        publish("UPDATE");
    }

    @AfterReturning("@annotation(deleteMapping) && within(org.godigit.policyvault.controller..*)")
    public void afterDelete(JoinPoint jp, DeleteMapping deleteMapping) {
        if (!isPoliciesEndpoint()) return;
        publish("DELETE");
    }

    private void publish(String action) {
        String uri = request.getRequestURI();
        UUID policyId = extractUuid(uri).orElse(null);
        String desc = request.getMethod() + " " + uri;
        publisher.publish(action, policyId, desc);
    }

    private boolean isPoliciesEndpoint() {
        String uri = request.getRequestURI();
        return uri != null && uri.startsWith("/policies");
    }

    private Optional<UUID> extractUuid(String uri) {
        if (uri == null) return Optional.empty();
        Matcher m = UUID_IN_PATH.matcher(uri);
        if (m.find()) {
            try {
                return Optional.of(UUID.fromString(m.group(1)));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return Optional.empty();
    }
}