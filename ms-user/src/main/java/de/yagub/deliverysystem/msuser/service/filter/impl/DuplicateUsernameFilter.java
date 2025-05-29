package de.yagub.deliverysystem.msuser.service.filter.impl;

import de.yagub.deliverysystem.msuser.dto.request.RegistrationRequest;
import de.yagub.deliverysystem.msuser.error.UsernameAlreadyExistsException;
import de.yagub.deliverysystem.msuser.model.FilterTarget;
import de.yagub.deliverysystem.msuser.model.enums.FilterId;
import de.yagub.deliverysystem.msuser.repository.UserRepository;
import de.yagub.deliverysystem.msuser.service.filter.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Order(2)
public class DuplicateUsernameFilter implements Filter{

    private final UserRepository userRepository;

    @Override
    public void execute(FilterTarget filterTarget) {
        String username = filterTarget.getRegistrationRequest().username();
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
    }

    @Override
    public FilterId id() {
        return FilterId.DUPLICATE_USERNAME_FILTER;
    }
}
