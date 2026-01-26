package com.stefanos.rcms.cases;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CaseRecordRepository extends JpaRepository<CaseRecord, Long>, JpaSpecificationExecutor<CaseRecord> {
    Optional<CaseRecord> findByExternalReference(String externalReference);
    boolean existsByExternalReference(String externalReference);

    @Query("select c.status as status, count(c) as count from CaseRecord c group by c.status")
    List<CaseStatusCount> countByStatus();
}
