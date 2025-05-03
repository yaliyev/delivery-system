package de.yagub.deliverysystem.msuser.dto.response;

public record LoginResponse(
        Long id,
        String username,
        boolean enabled
) {}