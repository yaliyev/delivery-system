package de.yagub.deliverysystem.msprocessmanager.model.msuser;

import lombok.Builder;

@Builder
public record LoginResponse(
        String username,
        boolean enabled
) {}
