package de.yagub.deliverysystem.msuser.service;



import de.yagub.deliverysystem.msuser.mapper.UserMapper;
import de.yagub.deliverysystem.msuser.dto.request.LoginRequest;
import de.yagub.deliverysystem.msuser.dto.request.RegistrationRequest;
import de.yagub.deliverysystem.msuser.dto.response.LoginResponse;
import de.yagub.deliverysystem.msuser.dto.response.UserResponse;
import de.yagub.deliverysystem.msuser.error.InvalidUserCredentialsException;
import de.yagub.deliverysystem.msuser.error.UsernameAlreadyExistsException;
import de.yagub.deliverysystem.msuser.model.User;
import de.yagub.deliverysystem.msuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    public UserResponse register(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException(request.username());
        }

        String passwordHash = passwordEncoder.encode(request.password());
        var user = User.builder()
                .username(request.username())
                .passwordHash(passwordHash)
                .enabled(true)
                .build();

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        var user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new InvalidUserCredentialsException("Invalid username and password"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new InvalidUserCredentialsException(("Invalid username and password"));
        }

        return userMapper.toLoginResponse(user);

    }

    @Override
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPasswordHash())
                        .disabled(!user.isEnabled())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
