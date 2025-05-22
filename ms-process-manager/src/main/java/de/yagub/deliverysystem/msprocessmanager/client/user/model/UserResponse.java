package de.yagub.deliverysystem.msprocessmanager.client.user.model;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String username,
        boolean enabled
) {}

