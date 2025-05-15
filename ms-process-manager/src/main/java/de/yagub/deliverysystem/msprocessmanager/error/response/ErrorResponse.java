package de.yagub.deliverysystem.msprocessmanager.error.response;

import de.yagub.deliverysystem.msprocessmanager.error.response.ErrorCode;

import java.time.Instant;
import java.util.UUID;


public record ErrorResponse(
        ErrorCode errorCode,
        String message,
        UUID uuid,
        String path,
        Instant timestamp
) {
    public ErrorResponse(ErrorCode errorCode, String message, UUID uuid, String path, Instant timestamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.uuid = uuid;
        this.path = path;
        this.timestamp = timestamp;
    }

    public ErrorResponse(ErrorCode errorCode, String message, String path) {
        this(errorCode, message, UUID.randomUUID(), path, Instant.now());
    }
}
