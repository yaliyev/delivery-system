package de.yagub.deliverysystem.msprocessmanager.client.user;



import de.yagub.deliverysystem.msprocessmanager.config.FeignConfig;
import de.yagub.deliverysystem.msprocessmanager.error.UserServiceException;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.LoginRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.LoginResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.RegistrationRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.UserResponse;
import feign.error.ErrorCodes;
import feign.error.ErrorHandling;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name          = "user-service",
        url           = "${user.service.url}",
        configuration = FeignConfig.class
)
public interface UserServiceClient {

    @ErrorHandling(
            codeSpecific = @ErrorCodes(codes = {400, 409}, generate = UserServiceException.class),
            defaultException = RuntimeException.class
    )
    @PostMapping("/register")
    UserResponse register(
            @RequestBody RegistrationRequest request
    );

    @ErrorHandling(
            codeSpecific = @ErrorCodes(codes = {400, 403}, generate = UserServiceException.class),
            defaultException = RuntimeException.class
    )
    @PostMapping("/login")
    LoginResponse login(
            @RequestBody LoginRequest request,@RequestHeader("Authorization") String authHeader
    );
}
