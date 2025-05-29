package de.yagub.deliverysystem.mswallet.service;

import de.yagub.deliverysystem.mswallet.dto.request.PaymentRequest;
import de.yagub.deliverysystem.mswallet.dto.response.WalletResponse;
import de.yagub.deliverysystem.mswallet.model.PaymentType;

public interface PaymentStrategy {
    WalletResponse payment(PaymentRequest paymentRequest);
    PaymentType getPaymentType();
}
