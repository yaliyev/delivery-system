package de.yagub.deliverysystem.msprocessmanager.controller;

import de.yagub.deliverysystem.msprocessmanager.model.msuser.RegistrationRequest;
import de.yagub.deliverysystem.msprocessmanager.model.msuser.UserResponse;
import de.yagub.deliverysystem.msprocessmanager.service.orchestration.msuser.UserOrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import de.yagub.deliverysystem.msprocessmanager.model.msuser.LoginRequest;
import de.yagub.deliverysystem.msprocessmanager.model.msuser.LoginResponse;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserOrchestrationService userOrchestrationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody RegistrationRequest request) {
        return userOrchestrationService.registerUser(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse loginUser(@RequestBody LoginRequest request) {
        return userOrchestrationService.loginUser(request);
    }

}
