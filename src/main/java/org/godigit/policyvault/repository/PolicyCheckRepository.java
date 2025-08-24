package org.godigit.policyvault.repository;

import org.godigit.policyvault.entities.PolicyCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyCheckRepository extends JpaRepository<PolicyCheck, Long> {
}