package de.yagub.deliverysystem.msuser.infrastructure.dto;

public record UserResponse(
        Long id,
        String username,
        boolean enabled
) {}
