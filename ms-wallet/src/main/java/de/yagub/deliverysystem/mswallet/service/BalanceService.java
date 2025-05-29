package de.yagub.deliverysystem.mswallet.service;

import de.yagub.deliverysystem.mswallet.dto.request.PaymentRequest;
import de.yagub.deliverysystem.mswallet.dto.request.UpdateBalanceRequest;
import de.yagub.deliverysystem.mswallet.dto.response.WalletResponse;
import de.yagub.deliverysystem.mswallet.error.InsufficientFundsException;
import de.yagub.deliverysystem.mswallet.error.WalletInactiveException;
import de.yagub.deliverysystem.mswallet.error.WalletNotFoundException;
import de.yagub.deliverysystem.mswallet.error.WalletUpdateFailedException;
import de.yagub.deliverysystem.mswallet.mapper.WalletMapper;
import de.yagub.deliverysystem.mswallet.model.PaymentType;
import de.yagub.deliverysystem.mswallet.model.Wallet;
import de.yagub.deliverysystem.mswallet.model.WalletStatus;
import de.yagub.deliverysystem.mswallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class BalanceService implements PaymentStrategy {

    private final WalletRepository walletRepository;

    private final WalletMapper walletMapper;


    public WalletResponse depositFunds(UpdateBalanceRequest request) {
        return performBalanceOperation(request.walletId(), request.amount(), "DEPOSIT");
    }

    private WalletResponse performBalanceOperation(Long walletId, BigDecimal amount, String operationType) {

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with ID: " + walletId));


        validateWalletStatus(wallet);
        if (operationType.equals("WITHDRAW")) {
            validateSufficientBalance(wallet, amount.abs());
        }


        int updatedRows = walletRepository.updateBalance(walletId, amount);


        if (updatedRows == 0) {
            throw new WalletUpdateFailedException("Wallet update failed - possible concurrent modification");
        }


        return walletMapper.toResponse(
                walletRepository.findById(walletId)
                        .orElseThrow(() -> new WalletNotFoundException("Wallet not found after successful update"))
        );
    }

    private void validateSufficientBalance(Wallet wallet, BigDecimal amount) {
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Current balance: " + wallet.getBalance() + ", Required: " + amount
            );
        }
    }

    private void validateWalletStatus(Wallet wallet) {
        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new WalletInactiveException("Wallet is not active. Current status: " + wallet.getStatus());
        }
    }

    public WalletResponse withdrawFunds(UpdateBalanceRequest request) {
        return performBalanceOperation(request.walletId(), request.amount().negate(), "WITHDRAW");
    }

    @Override
    public WalletResponse payment(PaymentRequest paymentRequest) {
        WalletResponse response = withdrawFunds(walletMapper.toUpdateBalance(paymentRequest));
        System.out.println("Money withdrawed...");
        return  response;
    }

    @Override
    public PaymentType getPaymentType(){
        return PaymentType.BALANCE;
    }
}
