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

    @PostMapping
    public ResponseEntity<UUID> createPolicy(@RequestBody PolicyCreateRequest request) {
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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ComplianceResponseDto uploadPolicy(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "policyName", required = false) String policyName,
            @RequestParam(value = "description", required = false) String description
    ) throws Exception {
        return complianceService.handleUploadAndCheck(file, policyName, description);
    }
}
