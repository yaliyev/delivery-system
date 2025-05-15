package de.yagub.deliverysystem.msprocessmanager.service;

import de.yagub.deliverysystem.msprocessmanager.client.user.model.LoginRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.LoginResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.RegistrationRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.UserResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessManagerService {

    private final UserServiceClient userServiceClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

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
//                , throwable -> {
//                    log.error("Registration failed for {} | Error: {} | Circuit Open: {}",
//                            request.username(),
//                            throwable.getMessage(),
//                            isCircuitOpen(throwable));  // Implement circuit state check
//                    throw new UserRegistrationException("Registration service unavailable", throwable);
//                }
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
//                , throwable -> {
//                    log.error("Login failed for {} | ErrorType: {} | Message: {}",
//                            request.username(),
//                            throwable.getClass().getSimpleName(),
//                            throwable.getMessage());
//                    throw new UserLoginException("Login service unavailable", throwable);
//                }
                );
    }
}