package com.stefanos.rcms.common;

import java.time.OffsetDateTime;

public class ErrorResponse {

    private String message;
    private String path;
    private OffsetDateTime timestamp;

    public ErrorResponse(String message, String path, OffsetDateTime timestamp) {
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}
