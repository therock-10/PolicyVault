package org.godigit.policyvault.search;

import org.godigit.policyvault.domain.Policy;
import org.godigit.policyvault.domain.PolicyVersion;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public final class PolicySpecifications {

    private PolicySpecifications() {}

    public static Specification<Policy> from(SearchRequest req) {
        return Specification.where(departmentEquals(req.getDepartment()))
                .and(keywordLike(req.getKeyword()))
                .and(updatedBetween(req));
    }

    public static Specification<Policy> departmentEquals(String department) {
        if (department == null) return null;
        final String dep = department.trim().toLowerCase();
        return (root, query, cb) -> cb.equal(cb.lower(root.get("department")), dep);
    }

    public static Specification<Policy> keywordLike(String keyword) {
        if (keyword == null) return null;
        final String like = "%" + keyword.trim().toLowerCase() + "%";
        return (root, query, cb) -> {
            // Join to versions to allow keyword search in content
            Join<Policy, PolicyVersion> versions = root.join("versions", JoinType.LEFT);
            query.distinct(true);
            Predicate inTitle = cb.like(cb.lower(root.get("title")), like);
            Predicate inContent = cb.like(cb.lower(versions.get("content")), like);
            return cb.or(inTitle, inContent);
        };
    }

    public static Specification<Policy> updatedBetween(SearchRequest req) {
        if (req.getFromDate() == null && req.getToDate() == null) return null;
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (req.getFromDate() != null) {
                OffsetDateTime start = OffsetDateTime.from(req.getFromDate().atStartOfDay(java.time.ZoneOffset.UTC));
                predicates.add(cb.greaterThanOrEqualTo(root.get("updatedAt"), start));
            }
            if (req.getToDate() != null) {
                OffsetDateTime end = OffsetDateTime.from(req.getToDate().plusDays(1).atStartOfDay(java.time.ZoneOffset.UTC).minusNanos(1));
                predicates.add(cb.lessThanOrEqualTo(root.get("updatedAt"), end));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}