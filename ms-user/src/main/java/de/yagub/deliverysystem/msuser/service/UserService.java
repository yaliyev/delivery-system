package de.yagub.deliverysystem.msuser.service;

import de.yagub.deliverysystem.msuser.dto.request.LoginRequest;
import de.yagub.deliverysystem.msuser.dto.request.RegistrationRequest;
import de.yagub.deliverysystem.msuser.dto.response.LoginResponse;
import de.yagub.deliverysystem.msuser.dto.response.UserResponse;
import de.yagub.deliverysystem.msuser.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    UserResponse register(RegistrationRequest request);
    LoginResponse login(LoginRequest loginRequest);
    boolean usernameExists(String username);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
