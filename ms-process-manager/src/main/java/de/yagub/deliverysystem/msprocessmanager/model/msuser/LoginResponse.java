package de.yagub.deliverysystem.msprocessmanager.model.msuser;

import lombok.Builder;

@Builder
public record LoginResponse(
        Long id,
        String username,
        boolean enabled
) {}
