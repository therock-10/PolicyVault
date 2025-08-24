package org.godigit.policyvault.service;

import org.godigit.policyvault.dto.ComplianceResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ComplianceService {
    ComplianceResponseDto handleUploadAndCheck(MultipartFile file, String policyName, String description) throws Exception;
}
