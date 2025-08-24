package org.godigit.policyvault.service.impl;

import org.godigit.policyvault.dto.PolicyUploadRequestDto;
import org.godigit.policyvault.entities.PolicyCheck;
import org.godigit.policyvault.mapper.PolicyUploadMapper;
import org.godigit.policyvault.repository.PolicyCheckRepository;
import org.godigit.policyvault.service.ComplianceCheckerService;
import org.godigit.policyvault.service.ComplianceService;
import org.godigit.policyvault.util.FileStorageUtil;
import org.godigit.policyvault.service.DocumentParserService;
import org.godigit.policyvault.dto.ComplianceResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class ComplianceServiceImpl implements ComplianceService {

    private final FileStorageUtil fileStorageUtil;
    private final DocumentParserService documentParserService;
    private final ComplianceCheckerService complianceCheckerService;
    private final PolicyCheckRepository policyCheckRepository;
    private final PolicyUploadMapper policyUploadMapper;

    public ComplianceServiceImpl(FileStorageUtil fileStorageUtil,DocumentParserService documentParserService, ComplianceCheckerService complianceCheckerService,PolicyCheckRepository policyRepository,PolicyUploadMapper policyUploadMapper) {
        this.fileStorageUtil = fileStorageUtil;
        this.documentParserService = documentParserService;
        this.complianceCheckerService = complianceCheckerService;
        this.policyCheckRepository = policyRepository;
        this.policyUploadMapper = policyUploadMapper;
    }

    @Transactional
    public ComplianceResponseDto handleUploadAndCheck(MultipartFile file, String policyName, String description) throws Exception {
        // 1) store file locally
        String filePath = fileStorageUtil.saveFile(file);

        // 2) extract text
        String text = documentParserService.extractText(new File(filePath));

        // 3) build DTO -> entity
        PolicyUploadRequestDto dto = new PolicyUploadRequestDto();
        dto.setPolicyName(policyName != null ? policyName : file.getOriginalFilename());
        dto.setDescription(description);
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
