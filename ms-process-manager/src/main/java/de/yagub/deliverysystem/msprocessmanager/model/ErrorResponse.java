package de.yagub.deliverysystem.msprocessmanager.model;

import de.yagub.deliverysystem.msprocessmanager.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
