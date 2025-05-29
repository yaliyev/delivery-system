package de.yagub.deliverysystem.msprocessmanager.client.wallet.model;

import java.math.BigDecimal;

public record UpdateBalanceRequest (
    Long walletId,
    BigDecimal amount
){}
