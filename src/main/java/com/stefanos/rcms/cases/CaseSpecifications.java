package com.stefanos.rcms.cases;

import org.springframework.data.jpa.domain.Specification;

public final class CaseSpecifications {

    private CaseSpecifications() {}

    public static Specification<CaseRecord> hasStatus(CaseStatus status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<CaseRecord> hasExternalReference(String externalReference) {
        return (root, query, cb) -> (externalReference == null || externalReference.isBlank())
            ? cb.conjunction()
            : cb.equal(root.get("externalReference"), externalReference);
    }

    public static Specification<CaseRecord> hasAssignedTo(String assignedTo) {
        return (root, query, cb) -> (assignedTo == null || assignedTo.isBlank())
            ? cb.conjunction()
            : cb.equal(root.get("assignedTo"), assignedTo);
    }
}
