package de.yagub.deliverysystem.mswallet.dto.request;

import java.math.BigDecimal;

public record CreateWalletRequest(
       Long userId,
       BigDecimal initialBalance,
       String currency
) {}
