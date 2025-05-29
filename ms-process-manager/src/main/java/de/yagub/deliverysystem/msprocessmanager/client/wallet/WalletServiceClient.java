package de.yagub.deliverysystem.msprocessmanager.client.wallet;

import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderRequest;
import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderResponse;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.CreateWalletRequest;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.UpdateBalanceRequest;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.WalletResponse;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.WalletStatus;
import de.yagub.deliverysystem.msprocessmanager.config.OrderFeignConfig;
import de.yagub.deliverysystem.msprocessmanager.config.WalletFeignConfig;
import de.yagub.deliverysystem.msprocessmanager.error.*;
import de.yagub.deliverysystem.msprocessmanager.error.response.BaseExceptionResponse;
import feign.error.ErrorCodes;
import feign.error.ErrorHandling;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "wallet-service",
        url = "${wallet.service.url}",
        configuration = WalletFeignConfig.class
)
public interface WalletServiceClient {


    @ErrorHandling(
            codeSpecific = {
                    @ErrorCodes(codes = {400}, generate = WalletBadRequestException.class),
                    @ErrorCodes(codes = {404}, generate = WalletNotFoundException.class),
                    @ErrorCodes(codes = {424}, generate = WalletFailedDependencyException.class)
            },
            defaultException = WalletProviderException.class
    )
    @PostMapping("")
    WalletResponse createWallet(@RequestBody CreateWalletRequest request);

    @ErrorHandling(
            codeSpecific = {
                    @ErrorCodes(codes = {400}, generate = WalletBadRequestException.class),
                    @ErrorCodes(codes = {404}, generate = WalletNotFoundException.class),
                    @ErrorCodes(codes = {424}, generate = WalletFailedDependencyException.class)
            },
            defaultException = WalletProviderException.class
    )
    @GetMapping("/{userId}")
    WalletResponse getWallet(@PathVariable Long userId);


    @ErrorHandling(
            codeSpecific = {
                    @ErrorCodes(codes = {400}, generate = WalletBadRequestException.class),
                    @ErrorCodes(codes = {404}, generate = WalletNotFoundException.class),
                    @ErrorCodes(codes = {424}, generate = WalletFailedDependencyException.class)
            },
            defaultException = WalletProviderException.class
    )
    @PostMapping("/deposit")
    WalletResponse depositFunds(@RequestBody UpdateBalanceRequest request);

    @ErrorHandling(
            codeSpecific = {
                    @ErrorCodes(codes = {400}, generate = WalletBadRequestException.class),
                    @ErrorCodes(codes = {404}, generate = WalletNotFoundException.class),
                    @ErrorCodes(codes = {424}, generate = WalletFailedDependencyException.class)
            },
            defaultException = WalletProviderException.class
    )
    @PostMapping("/withdraw")
    WalletResponse withdrawFunds(@RequestBody UpdateBalanceRequest request);

    @ErrorHandling(
            codeSpecific = {
                    @ErrorCodes(codes = {400}, generate = WalletBadRequestException.class),
                    @ErrorCodes(codes = {404}, generate = WalletNotFoundException.class),
                    @ErrorCodes(codes = {424}, generate = WalletFailedDependencyException.class)
            },
            defaultException = WalletProviderException.class
    )
    @PatchMapping("/{walletId}/status")
    WalletResponse updateWalletStatus(@PathVariable Long walletId, @RequestParam WalletStatus status);

    @ErrorHandling(
            codeSpecific = {
                    @ErrorCodes(codes = {400}, generate = WalletBadRequestException.class),
                    @ErrorCodes(codes = {404}, generate = WalletNotFoundException.class),
                    @ErrorCodes(codes = {424}, generate = WalletFailedDependencyException.class)
            },
            defaultException = WalletProviderException.class
    )
    @GetMapping
    List<WalletResponse> getAllWallets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @ErrorHandling(
            codeSpecific = {
                    @ErrorCodes(codes = {400}, generate = WalletBadRequestException.class),
                    @ErrorCodes(codes = {404}, generate = WalletNotFoundException.class),
                    @ErrorCodes(codes = {424}, generate = WalletFailedDependencyException.class)
            },
            defaultException = WalletProviderException.class
    )
    @DeleteMapping("/{walletId}")
    void deleteWallet(@PathVariable Long walletId);
}