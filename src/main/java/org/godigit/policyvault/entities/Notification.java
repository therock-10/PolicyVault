package org.godigit.policyvault.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity representing a notification stored in DB.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

//    @Column(nullable = true)
//    private UUID userId; // Store DEPT_HEAD user ID who should get this notification

    @Column(nullable = false)
    private String message;


    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        if(createdAt == null) {
            createdAt = Instant.now();
        }
    }

}
