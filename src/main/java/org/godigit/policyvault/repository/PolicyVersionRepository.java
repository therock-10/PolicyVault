package org.godigit.policyvault.repository;

import org.godigit.policyvault.domain.PolicyVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PolicyVersionRepository extends JpaRepository<PolicyVersion, UUID> {
    List<PolicyVersion> findByPolicyIdOrderByVersionDesc(UUID policyId);
    PolicyVersion findByPolicyIdAndVersion(UUID policyId, int version);
}
