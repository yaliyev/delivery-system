package de.yagub.deliverysystem.mswallet.service;

import de.yagub.deliverysystem.mswallet.dto.request.CreateWalletRequest;
import de.yagub.deliverysystem.mswallet.dto.request.UpdateBalanceRequest;
import de.yagub.deliverysystem.mswallet.dto.response.WalletResponse;
import de.yagub.deliverysystem.mswallet.error.*;
import de.yagub.deliverysystem.mswallet.mapper.WalletMapper;
import de.yagub.deliverysystem.mswallet.model.Wallet;
import de.yagub.deliverysystem.mswallet.model.WalletStatus;
import de.yagub.deliverysystem.mswallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Override
    public WalletResponse createWallet(CreateWalletRequest request) {
        // Check if wallet already exists
        walletRepository.findByUserId(request.userId()).ifPresent(w -> {
            throw new WalletAlreadyExistsException("Wallet already exists for user ID: " + request.userId());
        });

        Wallet wallet = walletMapper.toEntity(request);
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setUpdatedAt(LocalDateTime.now());
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setVersion(0L);

        Wallet savedWallet = walletRepository.create(wallet);
        return walletMapper.toResponse(savedWallet);
    }

    @Override
    public WalletResponse getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .map(walletMapper::toResponse)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user ID: " + userId));
    }

    @Override
    public WalletResponse depositFunds(UpdateBalanceRequest request) {
        return performBalanceOperation(request.walletId(), request.amount(), "DEPOSIT");
    }

    @Override
    public WalletResponse withdrawFunds(UpdateBalanceRequest request) {
        return performBalanceOperation(request.walletId(), request.amount().negate(), "WITHDRAW");
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

    @Override
    public WalletResponse updateWalletStatus(Long walletId, WalletStatus newStatus) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with ID: " + walletId));

        wallet.setStatus(newStatus);
        wallet.setUpdatedAt(LocalDateTime.now());

        Wallet updatedWallet = walletRepository.update(wallet).get();
        return walletMapper.toResponse(updatedWallet);
    }

    @Override
    public List<WalletResponse> getAllWallets(int page, int size) {
        int offset = page * size;
        return walletRepository.findAll(size, offset).stream()
                .map(walletMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWallet(Long walletId) {
        walletRepository.findById(walletId).ifPresent(wallet -> {
            wallet.setStatus(WalletStatus.INACTIVE);
            walletRepository.update(wallet);
        });
    }

    private void validateWalletStatus(Wallet wallet) {
        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new WalletInactiveException("Wallet is not active. Current status: " + wallet.getStatus());
        }
    }

    private void validateSufficientBalance(Wallet wallet, BigDecimal amount) {
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Current balance: " + wallet.getBalance() + ", Required: " + amount
            );
        }
    }
}
