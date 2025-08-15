package org.godigit.policyvault.controller;

import org.godigit.policyvault.dto.PolicyVersionResponse;
import org.godigit.policyvault.service.PolicyVersionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/policies/{id}/versions")
public class VersionController {

    private final PolicyVersionService versionService;

    public VersionController(PolicyVersionService versionService) {
        this.versionService = versionService;
    }

    @GetMapping
    public ResponseEntity<List<PolicyVersionResponse>> getAllVersions(@PathVariable UUID id) {
        return ResponseEntity.ok(versionService.getAllVersions(id));
    }

    @GetMapping("/{versionNumber}")
    public ResponseEntity<PolicyVersionResponse> getVersion(@PathVariable UUID id, @PathVariable int versionNumber) {
        return ResponseEntity.ok(versionService.getVersion(id, versionNumber));
    }
}
