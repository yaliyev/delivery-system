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




}