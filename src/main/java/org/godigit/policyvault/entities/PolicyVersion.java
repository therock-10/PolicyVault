package org.godigit.policyvault.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class PolicyVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    private int version;

    @Lob
    private String content;
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        id = UUID.randomUUID();
        createdAt = LocalDateTime.now();
    }
}

