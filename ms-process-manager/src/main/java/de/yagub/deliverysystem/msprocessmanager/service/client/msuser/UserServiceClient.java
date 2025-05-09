package de.yagub.deliverysystem.msprocessmanager.service.client.msuser;



import de.yagub.deliverysystem.msprocessmanager.config.FeignConfig;
import de.yagub.deliverysystem.msprocessmanager.error.CustomFeignException;
import de.yagub.deliverysystem.msprocessmanager.error.UserServiceException;
import de.yagub.deliverysystem.msprocessmanager.model.msuser.LoginRequest;
import de.yagub.deliverysystem.msprocessmanager.model.msuser.LoginResponse;
import de.yagub.deliverysystem.msprocessmanager.model.msuser.RegistrationRequest;
import de.yagub.deliverysystem.msprocessmanager.model.msuser.UserResponse;
import feign.error.ErrorCodes;
import feign.error.ErrorHandling;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@ErrorHandling(
        codeSpecific = {
                @ErrorCodes(codes = {400, 409}, generate = CustomFeignException.class)
        }
)
@FeignClient(
        name          = "user-service",
        url           = "${user.service.url}",
        configuration = FeignConfig.class
)
public interface UserServiceClient {

    @PostMapping("/register")
    UserResponse register(
            @RequestBody RegistrationRequest request
    );

    @PostMapping("/login")
    LoginResponse login(
            @RequestBody LoginRequest request,@RequestHeader("Authorization") String authHeader
    );
}
