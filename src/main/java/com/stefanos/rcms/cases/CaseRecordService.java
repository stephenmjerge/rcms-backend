package com.stefanos.rcms.cases;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.stefanos.rcms.audit.AuditLogService;
import com.stefanos.rcms.cases.dto.CaseCreateRequest;
import com.stefanos.rcms.cases.dto.CaseResponse;
import com.stefanos.rcms.cases.dto.CaseUpdateRequest;


@Service
@Transactional
public class CaseRecordService {

    private final CaseRecordRepository repository;
    private final AuditLogService auditLogService;

    public CaseRecordService(CaseRecordRepository repository, AuditLogService auditLogService) {
        this.repository = repository;
        this.auditLogService = auditLogService;
    }


    public CaseResponse create(CaseCreateRequest request) {
        if (repository.existsByExternalReference(request.getExternalReference())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "externalReference already exists");
        }

        CaseRecord record = new CaseRecord();
        record.setExternalReference(request.getExternalReference());
        record.setTitle(request.getTitle());
        record.setDescription(request.getDescription());
        record.setStatus(request.getStatus());

        CaseRecord saved = repository.save(record);
        auditLogService.record("CREATE", "CaseRecord", saved.getId().toString(),
            "externalReference=" + saved.getExternalReference());
        return toResponse(saved);

    }

    public CaseResponse getById(Long id) {
        CaseRecord record = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Case not found"));
        auditLogService.record("VIEW", "CaseRecord", record.getId().toString(), null);
        return toResponse(record);

    }

    @Transactional(readOnly = true)
    public Page<CaseResponse> listAll(CaseStatus status, String externalReference, Pageable pageable) {
        Specification<CaseRecord> spec = Specification
            .where(CaseSpecifications.hasStatus(status))
            .and(CaseSpecifications.hasExternalReference(externalReference));

        return repository.findAll(spec, pageable)
            .map(this::toResponse);
    }

    public CaseResponse update(Long id, CaseUpdateRequest request) {
        CaseRecord record = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Case not found"));

        record.setTitle(request.getTitle());
        record.setDescription(request.getDescription());
        record.setStatus(request.getStatus());

        CaseRecord saved = repository.save(record);
        auditLogService.record("UPDATE", "CaseRecord", saved.getId().toString(), null);
        return toResponse(saved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Case not found");
        }
        repository.deleteById(id);
    }

    private CaseResponse toResponse(CaseRecord record) {
        CaseResponse response = new CaseResponse();
        response.setId(record.getId());
        response.setExternalReference(record.getExternalReference());
        response.setTitle(record.getTitle());
        response.setDescription(record.getDescription());
        response.setStatus(record.getStatus());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        return response;
    }
}
