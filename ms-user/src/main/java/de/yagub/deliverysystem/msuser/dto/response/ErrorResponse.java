package de.yagub.deliverysystem.msuser.dto.response;

import de.yagub.deliverysystem.msuser.exception.ErrorCode;

import java.time.Instant;
import java.util.UUID;

public record ErrorResponse(
        ErrorCode errorCode,
        String message,
        UUID errorId,
        String path,
        Instant timestamp
) {
    public ErrorResponse(ErrorCode errorCode, String message, String path) {
        this(errorCode, message, UUID.randomUUID(), path, Instant.now());
    }
}
