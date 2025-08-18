package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.entities.Policy;
import org.godigit.policyvault.entities.PolicyVersion;
import org.godigit.policyvault.entities.ChangeLog;
import org.godigit.policyvault.dto.*;
import org.godigit.policyvault.repository.*;
import org.godigit.policyvault.service.PolicyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepo;
    private final PolicyVersionRepository versionRepo;
    private final ChangeLogRepository changeLogRepo;

    public PolicyServiceImpl(PolicyRepository policyRepo, PolicyVersionRepository versionRepo, ChangeLogRepository changeLogRepo) {
        this.policyRepo = policyRepo;
        this.versionRepo = versionRepo;
        this.changeLogRepo = changeLogRepo;
    }

    @Override
    @Transactional
    public UUID createPolicy(PolicyCreateRequest request) {
        var policy = new Policy();
        policy.setTitle(request.title());
        policy.setDepartment(request.department());
        policy.setCurrentVersion(1);
        policyRepo.save(policy);

        var version = new PolicyVersion();
        version.setPolicy(policy);
        version.setVersion(1);
        version.setContent(request.content());
        versionRepo.save(version);

        return policy.getId();
    }

    @Override
    public PolicyResponse getPolicy(UUID id) {
        var policy = policyRepo.findById(id).orElseThrow();
        return new PolicyResponse(policy.getId(), policy.getTitle(), policy.getDepartment(),
                policy.getCurrentVersion(), policy.getCreatedAt(), policy.getUpdatedAt());
    }

    @Override
    @Transactional
    public void updatePolicy(UUID id, PolicyUpdateRequest request) {
        var policy = policyRepo.findById(id).orElseThrow();
        int newVersion = policy.getCurrentVersion() + 1;

        var version = new PolicyVersion();
        version.setPolicy(policy);
        version.setVersion(newVersion);
        version.setContent(request.content());
        versionRepo.save(version);

        var changeLog = new ChangeLog();
        changeLog.setPolicy(policy);
        changeLog.setOldVersion(policy.getCurrentVersion());
        changeLog.setNewVersion(newVersion);
        changeLog.setChangedBy(request.changedBy());
        changeLog.setDescription(request.description());
        changeLogRepo.save(changeLog);

        policy.setCurrentVersion(newVersion);
        policyRepo.save(policy);
    }

    @Override
    public void deletePolicy(UUID id) {
        policyRepo.deleteById(id);
    }

    @Override
    public List<PolicyResponse> searchPolicies(String department, String keyword) {
        var policies = policyRepo.findAll();
        return policies.stream()
                .filter(p -> (department == null || p.getDepartment().toLowerCase().contains(department.toLowerCase())) &&
                        (keyword == null || p.getTitle().toLowerCase().contains(keyword.toLowerCase())))
                .map(p -> new PolicyResponse(p.getId(), p.getTitle(), p.getDepartment(),
                        p.getCurrentVersion(), p.getCreatedAt(), p.getUpdatedAt()))
                .toList();
    }
}
