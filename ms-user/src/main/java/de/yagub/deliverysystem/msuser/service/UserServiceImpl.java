package de.yagub.deliverysystem.msuser.service;



import de.yagub.deliverysystem.msuser.mapper.UserMapper;
import de.yagub.deliverysystem.msuser.dto.request.LoginRequest;
import de.yagub.deliverysystem.msuser.dto.request.RegistrationRequest;
import de.yagub.deliverysystem.msuser.dto.response.LoginResponse;
import de.yagub.deliverysystem.msuser.dto.response.UserResponse;
import de.yagub.deliverysystem.msuser.error.InvalidUserCredentialsException;
import de.yagub.deliverysystem.msuser.error.UsernameAlreadyExistsException;
import de.yagub.deliverysystem.msuser.model.FilterTarget;
import de.yagub.deliverysystem.msuser.model.User;
import de.yagub.deliverysystem.msuser.model.enums.FilterId;
import de.yagub.deliverysystem.msuser.repository.UserRepository;
import de.yagub.deliverysystem.msuser.service.filter.Filter;
import de.yagub.deliverysystem.msuser.service.filter.FilterChainBuilder;
import de.yagub.deliverysystem.msuser.service.filter.impl.DuplicateUsernameFilter;
import de.yagub.deliverysystem.msuser.service.filter.impl.PasswordStrengthFilter;
import de.yagub.deliverysystem.msuser.service.filter.impl.UsernameValidationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final FilterChainBuilder filterChainBuilder;

    private final DuplicateUsernameFilter duplicateUsernameFilter;

    private final PasswordStrengthFilter passwordStrengthFilter;

    private final UsernameValidationFilter usernameValidationFilter;

    @Override
    public UserResponse register(RegistrationRequest request) {

        List<FilterId> filterList =  filterChainBuilder.getUserServiceFilters();

        FilterTarget filterTarget = new FilterTarget(request);
        for(int i = 0;i < filterList.size();i++){

            FilterId filter = filterList.get(i);

            switch(filter){
                case DUPLICATE_USERNAME_FILTER -> {
                   duplicateUsernameFilter.execute(filterTarget);
                }
                case PASSWORD_STRENGTH_FILTER -> {
                    passwordStrengthFilter.execute(filterTarget);
                }
                case USERNAME_VALIDATION_FILTER -> {
                    usernameValidationFilter.execute(filterTarget);
                }
            }

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
