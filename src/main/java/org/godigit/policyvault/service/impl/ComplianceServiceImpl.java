package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.dto.PolicyUploadRequestDto;
import org.godigit.policyvault.entities.PolicyCheck;
import org.godigit.policyvault.entities.PolicyVersion;
import org.godigit.policyvault.exception.PolicyNotFoundException;
import org.godigit.policyvault.mapper.PolicyUploadMapper;
import org.godigit.policyvault.repository.PolicyCheckRepository;
import org.godigit.policyvault.repository.PolicyRepository;
import org.godigit.policyvault.repository.PolicyVersionRepository;
import org.godigit.policyvault.service.ComplianceCheckerService;
import org.godigit.policyvault.service.ComplianceService;
import org.godigit.policyvault.util.FileStorageUtil;
import org.godigit.policyvault.service.DocumentParserService;
import org.godigit.policyvault.dto.ComplianceResponseDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class ComplianceServiceImpl implements ComplianceService {

    private final DocumentParserService documentParserService;
    private final ComplianceCheckerService complianceCheckerService;
    private final PolicyVersionRepository policyVersionRepository;
    private final PolicyCheckRepository policyCheckRepository;
    private final PolicyUploadMapper policyUploadMapper;

    public ComplianceServiceImpl(DocumentParserService documentParserService, ComplianceCheckerService complianceCheckerService,PolicyVersionRepository policyVersionRepository, PolicyCheckRepository policyCheckRepository,PolicyUploadMapper policyUploadMapper) {
        this.documentParserService = documentParserService;
        this.complianceCheckerService = complianceCheckerService;
        this.policyVersionRepository = policyVersionRepository;
        this.policyCheckRepository = policyCheckRepository;
        this.policyUploadMapper = policyUploadMapper;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('COMPLIANCE_OFFICER', 'ADMIN')")
    public ComplianceResponseDto handleAutoCompliance(UUID id) throws Exception {
        // 1) store file locally
        PolicyVersion policyVersion = policyVersionRepository.findById(id).orElseThrow(() -> new PolicyNotFoundException("Policy with ID '" + id + "' not found"));

        String filePath = policyVersion.getFile();

        // 2) extract text
        String text = documentParserService.extractText(new File(filePath));

        // 3) build entity from dto
        PolicyUploadRequestDto dto = new PolicyUploadRequestDto();
        String policyName = policyVersion.getPolicy().getTitle();
        dto.setPolicyName(policyName);
        dto.setPolicyText(text);

        PolicyCheck entity = policyUploadMapper.toEntity(dto);
        entity.setFilePath(filePath);

        // 4) call LLM
        ComplianceResponseDto result = complianceCheckerService.checkCompliance(text);

        // 5) persist results
        entity.setCompliant(result.isCompliant());
        entity.setReason(result.getReason());
        entity.setViolations(result.getViolations());
        policyCheckRepository.save(entity);

        // 6) return response DTO
        return policyUploadMapper.toDto(entity);
    }
}
