package de.yagub.deliverysystem.msprocessmanager.client.user.model;

import lombok.Builder;

@Builder
public record LoginResponse(
        String username,
        boolean enabled
) {}
