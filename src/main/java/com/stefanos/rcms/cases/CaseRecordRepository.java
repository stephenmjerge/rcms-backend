package com.stefanos.rcms.cases;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseRecordRepository extends JpaRepository<CaseRecord, Long> {
    Optional<CaseRecord> findByExternalReference(String externalReference);
    boolean existsByExternalReference(String externalReference);
}
