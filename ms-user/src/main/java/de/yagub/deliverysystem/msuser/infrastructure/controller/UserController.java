package de.yagub.deliverysystem.msuser.infrastructure.controller;

import de.yagub.deliverysystem.msuser.domain.service.UserService;
import de.yagub.deliverysystem.msuser.infrastructure.dto.LoginRequest;
import de.yagub.deliverysystem.msuser.infrastructure.dto.LoginResponse;
import de.yagub.deliverysystem.msuser.infrastructure.dto.RegistrationRequest;
import de.yagub.deliverysystem.msuser.infrastructure.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @RequestBody RegistrationRequest request
    ) {
        var user = userService.register(
                request.username(),
                request.password()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.isEnabled()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestBody @Valid LoginRequest request
    ) {
        var user = userService.login(
                request.username(),
                request.password()
        );

        return ResponseEntity.ok(new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.isEnabled()
        ));
    }
}

