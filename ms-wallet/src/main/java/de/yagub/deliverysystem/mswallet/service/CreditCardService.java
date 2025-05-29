package de.yagub.deliverysystem.mswallet.service;

import de.yagub.deliverysystem.mswallet.dto.request.PaymentRequest;
import de.yagub.deliverysystem.mswallet.dto.request.UpdateBalanceRequest;
import de.yagub.deliverysystem.mswallet.dto.response.WalletResponse;
import de.yagub.deliverysystem.mswallet.mapper.WalletMapper;
import de.yagub.deliverysystem.mswallet.model.PaymentType;
import de.yagub.deliverysystem.mswallet.model.Wallet;
import de.yagub.deliverysystem.mswallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreditCardService implements PaymentStrategy {
    private final WalletRepository walletRepository;

    private final BalanceService balanceService;

    private final WalletMapper walletMapper;

    public WalletResponse buy(UpdateBalanceRequest request){
        WalletResponse response = balanceService.withdrawFunds(request);
        System.out.println("Something bought...");
        return  response;
    }


    @Override
    public WalletResponse payment(PaymentRequest paymentRequest) {
       return buy(walletMapper.toUpdateBalance(paymentRequest));
    }

    @Override
    public PaymentType getPaymentType() {
        return PaymentType.CREDIT_CARD;
    }
}
