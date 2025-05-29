package de.yagub.deliverysystem.msprocessmanager.client.wallet.model;

import java.math.BigDecimal;

public record CreateWalletRequest(
       Long userId,
       BigDecimal initialBalance,
       String currency
) {}
