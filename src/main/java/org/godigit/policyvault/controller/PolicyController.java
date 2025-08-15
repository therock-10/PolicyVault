package org.godigit.policyvault.controller;

import org.godigit.policyvault.dto.*;
import org.godigit.policyvault.service.PolicyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public ResponseEntity<UUID> createPolicy(@RequestBody @Valid PolicyCreateRequest request) {
        var id = policyService.createPolicy(request);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> getPolicy(@PathVariable UUID id) {
        return ResponseEntity.ok(policyService.getPolicy(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePolicy(@PathVariable UUID id, @RequestBody @Valid PolicyUpdateRequest request) {
        policyService.updatePolicy(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable UUID id) {
        policyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PolicyResponse>> searchPolicies(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(policyService.searchPolicies(department, keyword));
    }
}
