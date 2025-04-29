package de.yagub.deliverysystem.msuser.controller;

import de.yagub.deliverysystem.msuser.service.UserService;
import de.yagub.deliverysystem.msuser.dto.request.LoginRequest;
import de.yagub.deliverysystem.msuser.dto.response.LoginResponse;
import de.yagub.deliverysystem.msuser.dto.request.RegistrationRequest;
import de.yagub.deliverysystem.msuser.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@RequestBody RegistrationRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse loginUser(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}

