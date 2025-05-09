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
        return circuitBreakerFactory.create("user-service-register")
                .run(() -> {
                    log.debug("Attempting user registration for {}", request.username());
                    UserResponse response = userServiceClient.register(request);
                    log.info("User registered successfully: {}", response.username());
                    return response;
                }
//                , throwable -> {
//                    log.error("User registration failed for {}", request.username(), throwable);
//                    throw new UserRegistrationException("User service unavailable");
//                }
                );
    }

    public LoginResponse loginUser(LoginRequest request,String authHeader) {
        return circuitBreakerFactory.create("user-service-login")
                .run(() -> {
                    log.debug("Attempting login for {}", request.username());
                    LoginResponse response = userServiceClient.login(request,authHeader);
                    log.info("User logged in: {}", request.username());
                    return response;
                }
//                , throwable -> {
//                    log.error("Login failed for {}", request.username(), throwable);
//                    throw new UserLoginException("Login service unavailable");
//                }
                );
    }
}