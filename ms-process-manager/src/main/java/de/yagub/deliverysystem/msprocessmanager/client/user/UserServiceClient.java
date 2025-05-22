package de.yagub.deliverysystem.msprocessmanager.client.user;



import de.yagub.deliverysystem.msprocessmanager.client.user.model.LoginRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.LoginResponse;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.RegistrationRequest;
import de.yagub.deliverysystem.msprocessmanager.client.user.model.UserResponse;
import de.yagub.deliverysystem.msprocessmanager.config.UserFeignConfig;
import de.yagub.deliverysystem.msprocessmanager.error.UserBadRequestException;
import de.yagub.deliverysystem.msprocessmanager.error.UserNotFoundException;
import de.yagub.deliverysystem.msprocessmanager.error.UserProviderException;
import de.yagub.deliverysystem.msprocessmanager.error.response.BaseExceptionResponse;
import feign.error.ErrorCodes;
import feign.error.ErrorHandling;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name          = "user-service",
        url           = "${user.service.url}",
        configuration = UserFeignConfig.class
)
public interface UserServiceClient {

    @ErrorHandling(
            codeSpecific = {
                    @ErrorCodes(codes = {400}, generate = UserBadRequestException.class),
                    @ErrorCodes(codes = {404}, generate = UserNotFoundException.class)
            },
            defaultException = UserProviderException.class
    )
    @PostMapping("/register")
    UserResponse register(
            @RequestBody RegistrationRequest request
    );

    @ErrorHandling(
            codeSpecific = {
                    @ErrorCodes(codes = {400}, generate = UserBadRequestException.class),
                    @ErrorCodes(codes = {404}, generate = UserNotFoundException.class)
            },
            defaultException = UserProviderException.class
    )
    @PostMapping("/login")
    LoginResponse login(
            @RequestBody LoginRequest request,@RequestHeader("Authorization") String authHeader
    );
}
