package de.yagub.deliverysystem.msuser.validation;

import de.yagub.deliverysystem.msuser.dto.request.RegistrationRequest;
import de.yagub.deliverysystem.msuser.error.UsernameAlreadyExistsException;
import de.yagub.deliverysystem.msuser.repository.UserRepository;
import de.yagub.deliverysystem.msuser.service.filter.Filter;
import de.yagub.deliverysystem.msuser.service.filter.FilterChain;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Order(3)
public class DuplicateUsernameFilter implements Filter<RegistrationRequest> {

    private final UserRepository userRepository;

    @Override
    public void execute(RegistrationRequest context, FilterChain<RegistrationRequest> chain) {
        String username = context.username();
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
        chain.proceed(context);
    }
}
