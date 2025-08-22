package org.godigit.policyvault.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class AuditLog {
    @Id
    @GeneratedValue

    private UUID id;

    private String userId;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    private String action;
    private LocalDateTime timestamp;

    @PrePersist
    public void onCreate() {
        timestamp = LocalDateTime.now();
    }
}

