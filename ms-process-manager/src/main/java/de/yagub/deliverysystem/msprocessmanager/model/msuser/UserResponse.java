package de.yagub.deliverysystem.msprocessmanager.model.msuser;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String username,
        boolean enabled
) {}

