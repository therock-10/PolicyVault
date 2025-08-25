package org.godigit.policyvault.mapper;

import org.godigit.policyvault.dto.ComplianceResponseDto;
import org.godigit.policyvault.dto.PolicyUploadRequestDto;
import org.godigit.policyvault.entities.PolicyCheck;
import org.springframework.stereotype.Component;

@Component
public class PolicyUploadMapper {

    public PolicyCheck toEntity(PolicyUploadRequestDto dto) {
        PolicyCheck e = new PolicyCheck();
        e.setPolicyName(dto.getPolicyName());
        e.setPolicyText(dto.getPolicyText());
        return e;
    }

    public ComplianceResponseDto toDto(PolicyCheck e) {
        ComplianceResponseDto dto = new ComplianceResponseDto();
        dto.setId(e.getId());
        dto.setCompliant(e.isCompliant());
        dto.setReason(e.getReason());
        dto.setUploadedAt(e.getUploadedAt());
        dto.setViolations(e.getViolations());
        return dto;
    }
}