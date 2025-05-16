package de.yagub.deliverysystem.msprocessmanager.service;

import de.yagub.deliverysystem.msprocessmanager.client.order.OrderServiceClient;
import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderRequest;
import de.yagub.deliverysystem.msprocessmanager.client.order.model.OrderResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.LoginRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.LoginResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.RegistrationRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.UserResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessManagerService {

    private final UserServiceClient userServiceClient;

    private final OrderServiceClient orderServiceClient;
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
        log.trace("Getting Order by ID: {}",id);

        return circuitBreakerFactory.create("order-service-get-order-by-id")
                .run(() -> {

                            OrderResponse response = orderServiceClient.getOrderById(id);
                            log.info("Order received successfully");

                            log.trace("Order details: {}", response);
                            return response;
                        }
                );
    }

    public List<OrderResponse> getOrdersByCustomerId(@PathVariable String customerId){
        log.trace("Getting Orders by Customer ID: {}",customerId);

        return circuitBreakerFactory.create("order-service-get-orders-by-customer-id")
                .run(() -> {

                            List<OrderResponse> response = orderServiceClient.getOrdersByCustomer(customerId);
                            log.info("Orders received successfully");

                            log.trace("Orders : {}", response);
                            return response;
                        }
                );
    }


}