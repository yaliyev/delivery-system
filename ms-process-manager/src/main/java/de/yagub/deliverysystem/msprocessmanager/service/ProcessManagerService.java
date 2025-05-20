package de.yagub.deliverysystem.msprocessmanager.service;

import de.yagub.deliverysystem.msprocessmanager.client.order.OrderServiceClient;
import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderRequest;
import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.LoginRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.LoginResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.RegistrationRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.UserResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.UserServiceClient;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.WalletServiceClient;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.CreateWalletRequest;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.UpdateBalanceRequest;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.WalletResponse;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.model.WalletStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessManagerService {

    private final UserServiceClient userServiceClient;

    private final OrderServiceClient orderServiceClient;

    private final WalletServiceClient walletServiceClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    // user
    public UserResponse registerUser(RegistrationRequest request) {
        return circuitBreakerFactory.create("user-service-register")
                .run(() -> {
                            UserResponse response = userServiceClient.register(request);
                            return response;
                        }
                );
    }

    public LoginResponse loginUser(LoginRequest request, String authHeader) {
        return circuitBreakerFactory.create("user-service-login")
                .run(() -> {
                            LoginResponse response = userServiceClient.login(request, authHeader);
                            return response;
                        }
                );
    }

    // order
    public OrderResponse createOrder(OrderRequest request) {
        return circuitBreakerFactory.create("order-service-create-order")
                .run(() -> {
                            OrderResponse response = orderServiceClient.createOrder(request);
                            return response;
                        }
                );
    }

    public OrderResponse getOrderById(String id) {
        return circuitBreakerFactory.create("order-service-get-order-by-id")
                .run(() -> {
                            OrderResponse response = orderServiceClient.getOrderById(id);
                            return response;
                        }
                );
    }

    public List<OrderResponse> getOrdersByCustomerId(@PathVariable String customerId) {
        return circuitBreakerFactory.create("order-service-get-orders-by-customer-id")
                .run(() -> {
                            List<OrderResponse> response = orderServiceClient.getOrdersByCustomer(customerId);
                            return response;
                        }
                );
    }

    // wallet

    public WalletResponse createWallet(CreateWalletRequest request) {
        return circuitBreakerFactory.create("wallet-service-create-wallet")
                .run(() -> {
                            WalletResponse response = walletServiceClient.createWallet(request);
                            return response;
                        }
                );
    }

    public WalletResponse getWallet(@PathVariable Long userId) {
        return circuitBreakerFactory.create("wallet-service-get-wallet")
                .run(() -> {
                            WalletResponse response = walletServiceClient.getWallet(userId);
                            return response;
                        }
                );
    }

    public WalletResponse depositFunds(@RequestBody UpdateBalanceRequest request){
        return circuitBreakerFactory.create("wallet-service-deposit-funds")
                .run(() -> {
                            WalletResponse response = walletServiceClient.depositFunds(request);
                            return response;
                        }
                );
    }

    public WalletResponse withdrawFunds(@RequestBody UpdateBalanceRequest request){
        return circuitBreakerFactory.create("wallet-service-withdraw-funds")
                .run(() -> {
                            WalletResponse response = walletServiceClient.withdrawFunds(request);
                            return response;
                        }
                );
    }

    public WalletResponse updateWalletStatus(@PathVariable Long walletId, @RequestParam WalletStatus status){
        return circuitBreakerFactory.create("wallet-service-update-wallet-status")
                .run(() -> {
                            WalletResponse response = walletServiceClient.updateWalletStatus(walletId,status);
                            return response;
                        }
                );
    }

    public List<WalletResponse> getAllWallets(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return circuitBreakerFactory.create("wallet-service-get-all-wallets")
                .run(() -> {
                            List<WalletResponse> response = walletServiceClient.getAllWallets(page, size);
                            return response;
                        }
                );
    }

    public void deleteWallet(@PathVariable Long walletId){
         circuitBreakerFactory.create("wallet-service-delete-wallet")
                .run(() -> {
                            walletServiceClient.deleteWallet(walletId);
                            return null;
                        }
                );
    }


}