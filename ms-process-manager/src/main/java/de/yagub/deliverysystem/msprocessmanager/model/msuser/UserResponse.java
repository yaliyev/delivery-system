package de.yagub.deliverysystem.msprocessmanager.model.msuser;

import lombok.Builder;

@Builder
public record UserResponse(
        String username,
        boolean enabled
) {}

