package de.yagub.deliverysystem.msprocessmanager.error.response;

import de.yagub.deliverysystem.msprocessmanager.error.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.UUID;


public record ErrorResponse(
        String errorCode,
        String message,
        UUID uuid,
        String path,
        Instant timestamp
) {
    public ErrorResponse(String errorCode, String message, UUID uuid, String path, Instant timestamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.uuid = uuid;
        this.path = path;
        this.timestamp = timestamp;
    }

    public ErrorResponse(String errorCode, String message, String path) {
        this(errorCode, message, UUID.randomUUID(), path, Instant.now());
    }

}
