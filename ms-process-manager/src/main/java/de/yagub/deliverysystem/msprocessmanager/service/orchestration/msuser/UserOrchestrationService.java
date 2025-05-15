package de.yagub.deliverysystem.msprocessmanager.service.orchestration.msuser;

import de.yagub.deliverysystem.msprocessmanager.model.msuser.*;
import de.yagub.deliverysystem.msprocessmanager.service.client.msuser.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserOrchestrationService {

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