package org.godigit.policyvault.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "policy_check")
public class PolicyCheck {
    @Id
    @GeneratedValue
    private UUID id;

    private String policyName;
    private String filePath;

    @Column(columnDefinition = "text")
    private String policyText;

    private boolean compliant;

    @Column(columnDefinition = "text")
    private String reason;

    private OffsetDateTime uploadedAt = OffsetDateTime.now();

    private List<String> violations;
}