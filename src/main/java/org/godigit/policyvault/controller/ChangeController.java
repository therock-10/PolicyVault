package org.godigit.policyvault.controller;

import org.godigit.policyvault.dto.ChangeLogResponse;
import org.godigit.policyvault.service.ChangeLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/changes")
public class ChangeController {

    private final ChangeLogService changeLogService;

    public ChangeController(ChangeLogService changeLogService) {
        this.changeLogService = changeLogService;
    }

    @GetMapping
    public ResponseEntity<List<ChangeLogResponse>> getChanges(@RequestParam UUID policyId) {
        return ResponseEntity.ok(changeLogService.getChangeLogs(policyId));
    }
}
