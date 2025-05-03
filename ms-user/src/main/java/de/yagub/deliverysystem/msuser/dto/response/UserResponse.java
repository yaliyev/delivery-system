package de.yagub.deliverysystem.msuser.dto.response;

public record UserResponse(
        Long id,
        String username,
        boolean enabled
) {}
