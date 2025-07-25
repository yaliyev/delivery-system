package de.yagub.deliverysystem.mswallet.dto.response;

import de.yagub.deliverysystem.mswallet.model.WalletStatus;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Builder
public record WalletResponse(
        Long id,
        Long userId,
        BigDecimal balance,
        String currency,
        WalletStatus status,
        LocalDateTime createdAt
) implements Serializable {}
