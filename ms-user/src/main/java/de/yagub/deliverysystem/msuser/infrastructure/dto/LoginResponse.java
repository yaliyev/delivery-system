package de.yagub.deliverysystem.msuser.infrastructure.dto;

public record LoginResponse(
        Long id,
        String username,
        boolean enabled
) {}