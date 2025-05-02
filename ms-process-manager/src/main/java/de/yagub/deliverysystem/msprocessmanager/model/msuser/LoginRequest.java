package de.yagub.deliverysystem.msprocessmanager.model.msuser;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {}
