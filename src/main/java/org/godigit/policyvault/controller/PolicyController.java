package org.godigit.policyvault.controller;

import org.godigit.policyvault.dto.*;
import org.godigit.policyvault.service.ComplianceService;
import org.godigit.policyvault.service.PolicyService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/policies")
public class PolicyController {

    private final PolicyService policyService;
    private final ComplianceService complianceService;

    public PolicyController(PolicyService policyService, ComplianceService complianceService) {
        this.policyService = policyService;
        this.complianceService = complianceService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UUID> createPolicy(@ModelAttribute PolicyCreateRequest request) throws Exception {
        UUID id = policyService.createPolicy(request);
        return ResponseEntity.ok(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePolicy(@PathVariable UUID id, @ModelAttribute @Valid PolicyUpdateRequest request) throws Exception {
        policyService.updatePolicy(id, request);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> getPolicy(@PathVariable UUID id) {
        return ResponseEntity.ok(policyService.getPolicy(id));
    }

    @DeleteMapping("/{id}/delete")
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

    @PostMapping(value = "/{id}/auto-compliance-check")
    public ComplianceResponseDto uploadPolicy(@PathVariable UUID id) throws Exception {
        return complianceService.handleAutoCompliance(id);
    }
}
