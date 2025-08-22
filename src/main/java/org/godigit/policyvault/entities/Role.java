package org.godigit.policyvault.entities;

<<<<<<< HEAD
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 40)
    private String name; // e.g. "ROLE_ADMIN", "ROLE_EMPLOYEE"
}
=======
public enum Role {
    ROLE_ADMIN,
    ROLE_COMPLIANCE_OFFICER,
    ROLE_DEPT_HEAD,
    ROLE_EMPLOYEE
}
>>>>>>> ffbec8c1dffa682dcde92bfc7496e902e3ea8b6c
