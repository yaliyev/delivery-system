package de.yagub.deliverysystem.msprocessmanager.controller;

import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.CreateWalletRequest;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.UpdateBalanceRequest;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.WalletResponse;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.WalletStatus;
import de.yagub.deliverysystem.msprocessmanager.service.ProcessManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final ProcessManagerService processManagerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletResponse createWallet(@RequestBody CreateWalletRequest request) {
        return processManagerService.createWallet(request);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public WalletResponse getWallet(@PathVariable Long userId) {
        return processManagerService.getWallet(userId);
    }

    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    public WalletResponse depositFunds(@RequestBody UpdateBalanceRequest request) {
        return processManagerService.depositFunds(request);
    }

    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public WalletResponse withdrawFunds(@RequestBody UpdateBalanceRequest request) {
        return processManagerService.withdrawFunds(request);
    }

    @PatchMapping("/{walletId}/status")
    @ResponseStatus(HttpStatus.OK)
    public WalletResponse updateWalletStatus(
            @PathVariable Long walletId,
            @RequestParam WalletStatus status) {
        return processManagerService.updateWalletStatus(walletId, status);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WalletResponse> getAllWallets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return processManagerService.getAllWallets(page, size);
    }

    @DeleteMapping("/{walletId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWallet(@PathVariable Long walletId) {
        processManagerService.deleteWallet(walletId);
    }
}
