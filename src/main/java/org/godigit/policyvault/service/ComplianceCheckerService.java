package org.godigit.policyvault.service;

import org.godigit.policyvault.dto.ComplianceResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.IOException;

public interface ComplianceCheckerService{
    ComplianceResponseDto checkCompliance(String policyText) throws IOException, InterruptedException;
}
