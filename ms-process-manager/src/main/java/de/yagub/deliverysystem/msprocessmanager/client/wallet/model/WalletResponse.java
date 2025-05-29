package de.yagub.deliverysystem.msprocessmanager.client.wallet.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WalletResponse(
        Long id,
        Long userId,
        BigDecimal balance,
        String currency,
        WalletStatus status,
        LocalDateTime createdAt
) {}
