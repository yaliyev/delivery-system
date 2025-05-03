package de.yagub.deliverysystem.msuser.dto.mapper;

import de.yagub.deliverysystem.msuser.dto.request.LoginRequest;
import de.yagub.deliverysystem.msuser.dto.request.RegistrationRequest;
import de.yagub.deliverysystem.msuser.dto.response.LoginResponse;
import de.yagub.deliverysystem.msuser.dto.response.UserResponse;
import de.yagub.deliverysystem.msuser.model.User;

public class UserMapper {

    public static User toUser(RegistrationRequest request) {
        return User.builder()
                .username(request.username())
                .enabled(true)
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.isEnabled()
        );
    }
    public static User toUser(LoginRequest request) {
        return User.builder()
                .username(request.username())
                .build();
    }


    public static LoginResponse toLoginResponse(User user) {
        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.isEnabled()
        );
    }

}
