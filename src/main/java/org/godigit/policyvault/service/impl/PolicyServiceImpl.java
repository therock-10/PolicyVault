package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.entities.Policy;
import org.godigit.policyvault.entities.PolicyVersion;
import org.godigit.policyvault.entities.ChangeLog;
import org.godigit.policyvault.dto.*;
import org.godigit.policyvault.exception.PolicyNotFoundException;
import org.godigit.policyvault.repository.*;
import org.godigit.policyvault.service.NotificationService;
import org.godigit.policyvault.service.PolicyService;
import org.godigit.policyvault.util.FileStorageUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepo;
    private final PolicyVersionRepository versionRepo;
    private final ChangeLogRepository changeLogRepo;
    private final NotificationService notificationService;
    private final FileStorageUtil fileStorageUtil;
    private final DocumentParserServiceImpl documentParserService;
    private final AuditLogServiceImpl auditLogService;

    public PolicyServiceImpl(
            PolicyRepository policyRepo,
            PolicyVersionRepository versionRepo,
            ChangeLogRepository changeLogRepo,
            NotificationService notificationService,
            FileStorageUtil fileStorageUtil,
            DocumentParserServiceImpl documentParserService,
            AuditLogServiceImpl auditLogService
    ) {
        this.policyRepo = policyRepo;
        this.versionRepo = versionRepo;
        this.changeLogRepo = changeLogRepo;
        this.notificationService = notificationService;
        this.fileStorageUtil = fileStorageUtil;
        this.documentParserService = documentParserService;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('COMPLIANCE_OFFICER','ADMIN')")

    public UUID createPolicy(PolicyCreateRequest request) throws Exception {
        // 1. Store the file
        String filePath = fileStorageUtil.saveFile(request.file());

        // 2. Create and save policy
        var policy = new Policy();
        policy.setTitle(request.title());
        policy.setDepartment(request.department());
        policy.setCurrentVersion(1);
        policyRepo.save(policy);

        // 3. Create and save version
        var version = new PolicyVersion();
        version.setPolicy(policy);
        version.setVersion(1);
        version.setFile(filePath); // assuming PolicyVersion has this field
        versionRepo.save(version);

        // 4. Notify
        notificationService.createNotification(
                "Policy '" + policy.getTitle() + "' has been created."
        );

        auditLogService.log(policy.getId(), "CREATE");
        return policy.getId();
    }


    @Override
    @PreAuthorize("hasAnyRole('EMPLOYEE','DEPARTMENT_HEAD','COMPLIANCE_OFFICER','ADMIN')")
    public PolicyResponse getPolicy(UUID id) {
        var policy = policyRepo.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException("Policy with ID '" + id + "' not found"));

        auditLogService.log(policy.getId(), "GET POLICY with policy_id: " + policy.getId());
        return new PolicyResponse(
                policy.getId(),
                policy.getTitle(),
                policy.getDepartment(),
                policy.getCurrentVersion(),
                policy.getCreatedAt(),
                policy.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('COMPLIANCE_OFFICER','ADMIN')")
    public void updatePolicy(UUID id, PolicyUpdateRequest request) throws Exception {
        var policy = policyRepo.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException("Policy with ID '" + id + "' not found"));

        int newVersion = policy.getCurrentVersion() + 1;


        // 1. Store the file
        String filePath = fileStorageUtil.saveFile(request.file());

        // 2. Extract content
        String content = documentParserService.extractText(new File(filePath));

        // 3. Save new version
        var version = new PolicyVersion();
        version.setPolicy(policy);
        version.setVersion(newVersion);
        version.setFile(filePath); // assuming PolicyVersion has this field
        versionRepo.save(version);

        // 4. Save change log
        var changeLog = new ChangeLog();
        changeLog.setPolicy(policy);
        changeLog.setOldVersion(policy.getCurrentVersion());
        changeLog.setNewVersion(newVersion);
        changeLog.setChangedBy(request.changedBy());
        changeLog.setDescription(request.description());
        changeLogRepo.save(changeLog);

        // 5. Update policy
        policy.setCurrentVersion(newVersion);
        policyRepo.save(policy);

        // 6. Notify
        notificationService.createNotification(
                "Policy '" + policy.getTitle() + "' has been updated to version " + newVersion
        );
        auditLogService.log(policy.getId(), "UPDATED policy_id: " + policy.getId());
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePolicy(UUID id) {
        var policy = policyRepo.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException("Policy with ID '" + id + "' not found, cannot delete."));

        auditLogService.log(policy.getId(), "DELETE policy_id: " + policy.getId());
        notificationService.createNotification(
                "Policy '" + policy.getTitle() + "' has been deleted."
        );
        policyRepo.deleteById(id);

        notificationService.createNotification(
                "Policy '" + policy.getTitle() + "' has been deleted."
        );
    }

    @Override
    @PreAuthorize("hasAnyRole('EMPLOYEE','DEPARTMENT_HEAD','COMPLIANCE_OFFICER','ADMIN')")
    public List<PolicyResponse> searchPolicies(String department, String keyword) {
        var policies = policyRepo.findAll();

        return policies.stream()
                .filter(p -> (department == null || p.getDepartment().toLowerCase().contains(department.toLowerCase())) &&
                        (keyword == null || p.getTitle().toLowerCase().contains(keyword.toLowerCase())))
                .map(p -> new PolicyResponse(
                        p.getId(),
                        p.getTitle(),
                        p.getDepartment(),
                        p.getCurrentVersion(),
                        p.getCreatedAt(),
                        p.getUpdatedAt()
                ))
                .toList();
    }
}
