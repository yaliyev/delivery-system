package de.yagub.deliverysystem.msprocessmanager.service.client.msuser;

import de.yagub.deliverysystem.msprocessmanager.config.FeignConfig;
import de.yagub.deliverysystem.msprocessmanager.model.msuser.LoginRequest;
import de.yagub.deliverysystem.msprocessmanager.model.msuser.LoginResponse;
import de.yagub.deliverysystem.msprocessmanager.model.msuser.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import de.yagub.deliverysystem.msprocessmanager.model.msuser.RegistrationRequest;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "user-service",
        url = "${user.service.url}",
        configuration = FeignConfig.class
)
public interface UserServiceClient {

    @PostMapping("/register")
    UserResponse register(@RequestBody RegistrationRequest request);

    @PostMapping("/login")
    LoginResponse login(@RequestBody LoginRequest request,@RequestHeader("Authorization") String authHeader);
}
