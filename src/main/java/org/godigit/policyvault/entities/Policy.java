package org.godigit.policyvault.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Policy {
    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String department;
    private int currentVersion;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuditLog> auditLogs;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChangeLog> changeLogs;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PolicyVersion> versions;

    // NEW: who created and last updated this policy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "created_by_id",
            foreignKey = @ForeignKey(name = "fk_policy_created_by")
    )
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "updated_by_id",
            foreignKey = @ForeignKey(name = "fk_policy_updated_by")
    )
    private User lastUpdatedBy;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}