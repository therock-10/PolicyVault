package org.godigit.policyvault.service;

import org.godigit.policyvault.dto.ComplianceResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ComplianceService {
    ComplianceResponseDto handleAutoCompliance(UUID id) throws Exception;
}
