package com.stefanos.rcms.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stefanos.rcms.audit.dto.AuditLogResponse;

@RestController
@RequestMapping("/audit")
public class AuditLogController {

    private final AuditLogRepository repository;

    public AuditLogController(AuditLogRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{entityType}/{entityId}")
    public Page<AuditLogResponse> getAuditLog(
        @PathVariable String entityType,
        @PathVariable String entityId,
        Pageable pageable
    ) {
        return repository.findByEntityTypeAndEntityId(entityType, entityId, pageable)
            .map(this::toResponse);
    }

    private AuditLogResponse toResponse(AuditLog log) {
        AuditLogResponse response = new AuditLogResponse();
        response.setId(log.getId());
        response.setActor(log.getActor());
        response.setAction(log.getAction());
        response.setEntityType(log.getEntityType());
        response.setEntityId(log.getEntityId());
        response.setDetails(log.getDetails());
        response.setCreatedAt(log.getCreatedAt());
        return response;
    }
}
