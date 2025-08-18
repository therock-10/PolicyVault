package org.godigit.policyvault.repository;

import org.godigit.policyvault.entities.ChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, UUID> {
    List<ChangeLog> findByPolicyId(UUID policyId);
}
