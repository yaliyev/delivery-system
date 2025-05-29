package de.yagub.deliverysystem.mswallet.controller;

import de.yagub.deliverysystem.mswallet.dto.request.CreateWalletRequest;
import de.yagub.deliverysystem.mswallet.dto.request.PaymentRequest;
import de.yagub.deliverysystem.mswallet.dto.request.UpdateBalanceRequest;
import de.yagub.deliverysystem.mswallet.dto.response.WalletResponse;
import de.yagub.deliverysystem.mswallet.model.WalletStatus;
import de.yagub.deliverysystem.mswallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletResponse createWallet(@RequestBody CreateWalletRequest request) {
        return walletService.createWallet(request);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public WalletResponse getWallet(@PathVariable Long userId) {
        return walletService.getWalletByUserId(userId);
    }

//    @PostMapping("/deposit")
//    @ResponseStatus(HttpStatus.OK)
//    public WalletResponse depositFunds(@RequestBody UpdateBalanceRequest request) {
//        return walletService.depositFunds(request);
//    }
//
//    @PostMapping("/withdraw")
//    @ResponseStatus(HttpStatus.OK)
//    public WalletResponse withdrawFunds(@RequestBody UpdateBalanceRequest request) {
//        return walletService.withdrawFunds(request);
//    }

    @PatchMapping("/{walletId}/status")
    @ResponseStatus(HttpStatus.OK)
    public WalletResponse updateWalletStatus(
            @PathVariable Long walletId,
            @RequestParam WalletStatus status) {
        return walletService.updateWalletStatus(walletId, status);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WalletResponse> getAllWallets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return walletService.getAllWallets(page, size);
    }

    @DeleteMapping("/{walletId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWallet(@PathVariable Long walletId) {
        walletService.deleteWallet(walletId);
    }

    @PostMapping("/determinePayment")
    @ResponseStatus(HttpStatus.OK)
    public WalletResponse determinePayment(@RequestBody PaymentRequest paymentRequest){
        return walletService.determinePayment(paymentRequest);
    }
}
