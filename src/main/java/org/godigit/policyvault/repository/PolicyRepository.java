package org.godigit.policyvault.repository;

import org.godigit.policyvault.entities.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PolicyRepository extends JpaRepository<Policy, UUID> {
    List<Policy> findByDepartmentContainingIgnoreCase(String department);
    List<Policy> findByTitleContainingIgnoreCase(String keyword);
}
