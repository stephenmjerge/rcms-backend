package com.stefanos.rcms.cases.dto;

import com.stefanos.rcms.cases.CaseStatus;

public class CaseStatusCountResponse {

    private CaseStatus status;
    private long count;

    public CaseStatusCountResponse() {
    }

    public CaseStatusCountResponse(CaseStatus status, long count) {
        this.status = status;
        this.count = count;
    }

    public CaseStatus getStatus() {
        return status;
    }

    public void setStatus(CaseStatus status) {
        this.status = status;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
