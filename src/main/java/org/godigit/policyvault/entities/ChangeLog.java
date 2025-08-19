package org.godigit.policyvault.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ChangeLog {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    private int oldVersion;
    private int newVersion;
    private String changedBy;

    private String description;
    private LocalDateTime changeDate;

    @PrePersist
    public void onCreate() {
        changeDate = LocalDateTime.now();
    }
}
