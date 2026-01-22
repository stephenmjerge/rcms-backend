package com.stefanos.rcms.audit;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String actor;

    @Column(nullable = false, length = 32)
    private String action;

    @Column(name = "entity_type", nullable = false, length = 64)
    private String entityType;

    @Column(name = "entity_id", nullable = false, length = 64)
    private String entityId;

    @Column
    private String details;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    public Long getId() {
    return id;
}

public String getActor() {
    return actor;
}

public void setActor(String actor) {
    this.actor = actor;
}

public String getAction() {
    return action;
}

public void setAction(String action) {
    this.action = action;
}

public String getEntityType() {
    return entityType;
}

public void setEntityType(String entityType) {
    this.entityType = entityType;
}

public String getEntityId() {
    return entityId;
}

public void setEntityId(String entityId) {
    this.entityId = entityId;
}

public String getDetails() {
    return details;
}

public void setDetails(String details) {
    this.details = details;
}

public OffsetDateTime getCreatedAt() {
    return createdAt;
}

}
