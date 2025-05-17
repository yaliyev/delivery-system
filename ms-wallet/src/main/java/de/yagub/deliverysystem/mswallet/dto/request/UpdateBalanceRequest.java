package de.yagub.deliverysystem.mswallet.dto.request;

import java.math.BigDecimal;

public record UpdateBalanceRequest (
    Long walletId,
    BigDecimal amount
){}
