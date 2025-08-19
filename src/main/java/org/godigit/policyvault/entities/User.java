package org.godigit.policyvault.entities;

import jakarta.persistence.*;
import org.godigit.policyvault.entities.Role;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        }
)
public class User {

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    // getters/setters...
    // equals/hashCode on id
}