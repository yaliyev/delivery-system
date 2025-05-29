package de.yagub.deliverysystem.mswallet.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Wallet {
    private Long id;
    private Long userId;
    private BigDecimal balance;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private WalletStatus status;
    private Long version;
}
