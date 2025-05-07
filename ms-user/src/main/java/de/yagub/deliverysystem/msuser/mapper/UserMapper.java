package de.yagub.deliverysystem.msuser.mapper;

import de.yagub.deliverysystem.msuser.dto.request.LoginRequest;
import de.yagub.deliverysystem.msuser.dto.request.RegistrationRequest;
import de.yagub.deliverysystem.msuser.dto.response.LoginResponse;
import de.yagub.deliverysystem.msuser.dto.response.UserResponse;
import de.yagub.deliverysystem.msuser.model.User;

import de.yagub.deliverysystem.msuser.dto.request.LoginRequest;
import de.yagub.deliverysystem.msuser.dto.request.RegistrationRequest;
import de.yagub.deliverysystem.msuser.dto.response.LoginResponse;
import de.yagub.deliverysystem.msuser.dto.response.UserResponse;
import de.yagub.deliverysystem.msuser.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "enabled", constant = "true")
    User toUser(RegistrationRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "enabled", target = "enabled")
    UserResponse toUserResponse(User user);

    User toUser(LoginRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "enabled", target = "enabled")
    LoginResponse toLoginResponse(User user);
}


