package com.stefanos.rcms.cases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CaseRecordRepository extends JpaRepository<CaseRecord, Long>, JpaSpecificationExecutor<CaseRecord> {
    Optional<CaseRecord> findByExternalReference(String externalReference);
    boolean existsByExternalReference(String externalReference);
}
