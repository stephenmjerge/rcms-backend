package com.stefanos.rcms.cases.dto;

import com.stefanos.rcms.cases.CaseStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CaseCreateRequest {

    @NotBlank
    @Size(max = 64)
    private String externalReference;

    @NotBlank
    @Size(max = 255)
    private String title;

    private String description;

    @NotNull
    private CaseStatus status;

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CaseStatus getStatus() {
        return status;
    }

    public void setStatus(CaseStatus status) {
        this.status = status;
    }
}
