package org.godigit.policyvault.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class ChangeLog {
    @Id
    private UUID id;

    private UUID policyId;
    private int oldVersion;
    private int newVersion;
    private String changedBy;

    @Lob
    private String description;
    private LocalDateTime changeDate;

    @PrePersist
    public void onCreate() {
        id = UUID.randomUUID();
        changeDate = LocalDateTime.now();
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPolicyId() {
        return policyId;
    }

    public void setPolicyId(UUID policyId) {
        this.policyId = policyId;
    }

    public int getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(int oldVersion) {
        this.oldVersion = oldVersion;
    }

    public int getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(int newVersion) {
        this.newVersion = newVersion;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }
}
