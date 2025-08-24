package org.godigit.policyvault.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ComplianceResponseDto {
    @Id
    @GeneratedValue
    private UUID id;
    private boolean compliant;
    private String reason;
    private OffsetDateTime uploadedAt;
    private List<String> violations;
}