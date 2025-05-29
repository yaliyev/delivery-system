package de.yagub.deliverysystem.mswallet.service;

import de.yagub.deliverysystem.mswallet.dto.request.PaymentRequest;
import de.yagub.deliverysystem.mswallet.dto.request.TransferRequest;
import de.yagub.deliverysystem.mswallet.dto.response.WalletResponse;
import de.yagub.deliverysystem.mswallet.error.InsufficientFundsException;
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

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferService implements PaymentStrategy {
    private final WalletRepository walletRepository;

    private final WalletMapper walletMapper;

    public WalletResponse transferFunds(TransferRequest request){

        Optional<Wallet> fromWalletOpt = walletRepository.findById(request.fromUserId());
        Optional<Wallet> toWalletOpt = walletRepository.findById(request.toUserId());

        if (!fromWalletOpt.isPresent() || !toWalletOpt.isPresent()) {
            throw new WalletNotFoundException("One or both wallets not found");
        }

        Wallet fromWallet = fromWalletOpt.get();
        Wallet toWallet = toWalletOpt.get();


        if (fromWallet.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in source wallet");
        }


        if (!fromWallet.getCurrency().equals(toWallet.getCurrency())) {
            throw new WalletUpdateFailedException("Cannot transfer between different currencies");
        }


        if (fromWallet.getStatus() != WalletStatus.ACTIVE || toWallet.getStatus() != WalletStatus.ACTIVE) {
            throw new WalletUpdateFailedException("One or both wallets are not active");
        }

        walletRepository.transferFunds(request.fromUserId(), request.toUserId(), request.amount(), fromWallet.getVersion(), toWallet.getVersion());

        return walletMapper.toResponse(
                walletRepository.findById(request.fromUserId())
                        .orElseThrow(() -> new WalletNotFoundException("Wallet not found after successful update"))
        );
    }


    @Override
    public WalletResponse payment(PaymentRequest paymentRequest) {
        System.out.println("Money is transferring to someone...");
       WalletResponse response = transferFunds(walletMapper.toTransferRequest(paymentRequest));
       return response;
    }

    @Override
    public PaymentType getPaymentType() {
        return PaymentType.TRANSFER;
    }
}
