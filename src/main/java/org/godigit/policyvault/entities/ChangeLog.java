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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

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
}
