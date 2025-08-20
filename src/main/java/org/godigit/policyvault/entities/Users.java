package org.godigit.policyvault.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        }
)
public class Users {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable=false, length=60)
    private String username;

    @Column(nullable=false, length=120)
    private String passwordHash; // BCrypt

    @Column(nullable=false, length=120)
    private String email;

    @Column(length=120)
    private String department; // used to scope DEPT_HEAD/EMPLOYEE visibility

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id",
                    foreignKey = @ForeignKey(name = "fk_user_roles_user")))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable=false, length=40)
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant lastLoginAt;

    private boolean enabled = true;


    // getters/setters...
    // equals/hashCode on id
}