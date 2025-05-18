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

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessManagerService {

    private final UserServiceClient userServiceClient;

    private final OrderServiceClient orderServiceClient;

    private final WalletServiceClient walletServiceClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    // user
    public UserResponse registerUser(RegistrationRequest request) {
        log.trace("Entering registerUser for username: {}", request.username());

        return circuitBreakerFactory.create("user-service-register")
                .run(() -> {
                            log.debug("Initiating registration flow for: {}", request.username());

                            UserResponse response = userServiceClient.register(request);
                            log.info("Successfully registered user | Username: {}",
                                    response.username());

                            log.trace("Registration details: {}", response);
                            return response;
                        }
                );
    }

    public LoginResponse loginUser(LoginRequest request, String authHeader) {
        log.trace("Login attempt initiated | Username: {} | AuthHeaderPresent: {}",
                request.username(),
                !authHeader.isBlank());

        return circuitBreakerFactory.create("user-service-login")
                .run(() -> {
                            log.debug("Processing login for: {}", request.username());

                            LoginResponse response = userServiceClient.login(request, authHeader);
                            log.info("Successful login | Username: {}",
                                    request.username());

                            log.trace("Full login response: {}", response);
                            return response;
                        }
                );
    }

    // order
    public OrderResponse createOrder(OrderRequest request) {
        log.trace("Creating order");

        return circuitBreakerFactory.create("order-service-create-order")
                .run(() -> {

                            OrderResponse response = orderServiceClient.createOrder(request);
                            log.info("Successfully created order | Customer ID: {}",
                                    response.customerId());

                            log.trace("Order details: {}", response);
                            return response;
                        }
                );
    }

    public OrderResponse getOrderById(String id) {
        log.trace("Getting Order by ID: {}", id);

        return circuitBreakerFactory.create("order-service-get-order-by-id")
                .run(() -> {

                            OrderResponse response = orderServiceClient.getOrderById(id);
                            log.info("Order received successfully");

                            log.trace("Order details: {}", response);
                            return response;
                        }
                );
    }

    public List<OrderResponse> getOrdersByCustomerId(@PathVariable String customerId) {
        log.trace("Getting Orders by Customer ID: {}", customerId);

        return circuitBreakerFactory.create("order-service-get-orders-by-customer-id")
                .run(() -> {

                            List<OrderResponse> response = orderServiceClient.getOrdersByCustomer(customerId);
                            log.info("Orders received successfully");

                            log.trace("Orders : {}", response);
                            return response;
                        }
                );
    }

    // wallet

    public WalletResponse createWallet(CreateWalletRequest request) {
        log.trace("Creating  wallet for userId: {}", request.userId());

        return circuitBreakerFactory.create("wallet-service-create-wallet")
                .run(() -> {


                            WalletResponse response = walletServiceClient.createWallet(request);
                            log.info("Successfully created wallet | userId: {}",
                                    response.userId());

                            log.trace("Wallet details: {}", response);
                            return response;
                        }
                );
    }

    public WalletResponse getWallet(@PathVariable Long userId) {
        log.trace("Getting Wallet by User ID: {}", userId);

        return circuitBreakerFactory.create("wallet-service-get-wallet")
                .run(() -> {

                            WalletResponse response = walletServiceClient.getWallet(userId);
                            log.info("Wallet received successfully");

                            log.trace("Wallet : {}", response);
                            return response;
                        }
                );
    }

    public WalletResponse depositFunds(@RequestBody UpdateBalanceRequest request){
        log.trace("Depositing funds to Wallet ID: {}", request.walletId());

        return circuitBreakerFactory.create("wallet-service-deposit-funds")
                .run(() -> {

                            WalletResponse response = walletServiceClient.depositFunds(request);
                            log.info("Funds deposited successfully");

                            log.trace("Wallet : {}", response);
                            return response;
                        }
                );
    }

    public WalletResponse withdrawFunds(@RequestBody UpdateBalanceRequest request){
        log.trace("Withdrawing funds from Wallet ID: {}", request.walletId());

        return circuitBreakerFactory.create("wallet-service-withdraw-funds")
                .run(() -> {

                            WalletResponse response = walletServiceClient.withdrawFunds(request);
                            log.info("Funds withdrawed successfully");

                            log.trace("Wallet : {}", response);
                            return response;
                        }
                );
    }

    public WalletResponse updateWalletStatus(@PathVariable Long walletId, @RequestParam WalletStatus status){
        log.trace("Update Wallet status,wallet id  : {}", walletId);

        return circuitBreakerFactory.create("wallet-service-update-wallet-status")
                .run(() -> {

                            WalletResponse response = walletServiceClient.updateWalletStatus(walletId,status);
                            log.info("Wallet updated successfully");
                            log.trace("Wallet : {}", response);
                            return response;
                        }
                );
    }

    public List<WalletResponse> getAllWallets(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        log.trace("Getting All Wallets");

        return circuitBreakerFactory.create("wallet-service-get-all-wallets")
                .run(() -> {

                            List<WalletResponse> response = walletServiceClient.getAllWallets(page, size);
                            log.info("Successfully received all wallets");
                            log.trace("Wallets list : {}", response);
                            return response;
                        }
                );
    }

    public void deleteWallet(@PathVariable Long walletId){
        log.trace("Deleting wallet , walletId : {}",walletId);

         circuitBreakerFactory.create("wallet-service-delete-wallet")
                .run(() -> {

                            walletServiceClient.deleteWallet(walletId);
                            log.info("Successfully deleted wallet id: {}",walletId);
                            return null;
                        }
                );
    }


}