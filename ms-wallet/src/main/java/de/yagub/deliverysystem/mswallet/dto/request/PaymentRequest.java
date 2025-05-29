package de.yagub.deliverysystem.mswallet.dto.request;

import de.yagub.deliverysystem.mswallet.model.PaymentType;

import java.math.BigDecimal;

public record PaymentRequest (
    Long walletId,
    BigDecimal amount,
    String paymentType,
    Long receiverWalletId
    ){}
