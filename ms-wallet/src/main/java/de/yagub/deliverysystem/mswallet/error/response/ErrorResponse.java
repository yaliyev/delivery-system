package de.yagub.deliverysystem.mswallet.error.response;

import java.time.Instant;
import java.util.UUID;

public record ErrorResponse(
        ErrorCode errorCode,
        String message,
        UUID uuid,
        String path,
        Instant timestamp
) {
    public ErrorResponse(ErrorCode errorCode, String message, String path) {
        this(errorCode, message, UUID.randomUUID(), path, Instant.now());
    }
}