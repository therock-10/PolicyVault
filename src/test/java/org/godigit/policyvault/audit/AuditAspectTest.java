package org.godigit.policyvault.audit;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class AuditAspectTest {

    private HttpServletRequest request;
    private AuditEventPublisher publisher;
    private AuditAspect auditAspect;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        publisher = mock(AuditEventPublisher.class);
        auditAspect = new AuditAspect(request, publisher);
    }

    @Test
    void testAfterGetMapping_PoliciesEndpoint_ShouldPublishView() {
        String uri = "/policies/123e4567-e89b-12d3-a456-426614174000";
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn("GET");

        auditAspect.afterGet(null, mock(GetMapping.class));

        verify(publisher).publish(eq("VIEW"), eq(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")), eq("GET " + uri));
    }

    @Test
    void testAfterPostMapping_PoliciesEndpoint_ShouldPublishUpdate() {
        String uri = "/policies";
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn("POST");

        auditAspect.afterPost(null, mock(PostMapping.class));

        verify(publisher).publish(eq("UPDATE"), isNull(), eq("POST " + uri));
    }

    @Test
    void testAfterPutMapping_PoliciesEndpoint_ShouldPublishUpdate() {
        String uri = "/policies/123e4567-e89b-12d3-a456-426614174000";
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn("PUT");

        auditAspect.afterPut(null, mock(PutMapping.class));

        verify(publisher).publish(eq("UPDATE"), eq(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")), eq("PUT " + uri));
    }

    @Test
    void testAfterDeleteMapping_PoliciesEndpoint_ShouldPublishDelete() {
        String uri = "/policies/123e4567-e89b-12d3-a456-426614174000";
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn("DELETE");

        auditAspect.afterDelete(null, mock(DeleteMapping.class));

        verify(publisher).publish(eq("DELETE"), eq(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")), eq("DELETE " + uri));
    }

    @Test
    void testAfterGetMapping_NonPoliciesEndpoint_ShouldNotPublish() {
        when(request.getRequestURI()).thenReturn("/users");

        auditAspect.afterGet(null, mock(GetMapping.class));

        verifyNoInteractions(publisher);
    }

    @Test
    void testExtractUuid_InvalidUUID_ShouldReturnEmpty() {
        String uri = "/policies/not-a-uuid";
        Optional<UUID> result = auditAspect.extractUuid(uri);
        assert(result.isEmpty());
    }
}
