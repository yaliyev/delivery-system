package de.yagub.deliverysystem.mswallet.service;

import de.yagub.deliverysystem.mswallet.dto.request.CreateWalletRequest;
import de.yagub.deliverysystem.mswallet.dto.request.UpdateBalanceRequest;
import de.yagub.deliverysystem.mswallet.dto.response.WalletResponse;
import de.yagub.deliverysystem.mswallet.model.WalletStatus;

import java.util.List;

public interface WalletService {
    WalletResponse createWallet(CreateWalletRequest request);
    WalletResponse getWalletByUserId(Long userId);
    WalletResponse depositFunds(UpdateBalanceRequest request);
    WalletResponse withdrawFunds(UpdateBalanceRequest request);
    WalletResponse updateWalletStatus(Long walletId, WalletStatus newStatus);
    List<WalletResponse> getAllWallets(int page, int size);
    void deleteWallet(Long walletId);
}
