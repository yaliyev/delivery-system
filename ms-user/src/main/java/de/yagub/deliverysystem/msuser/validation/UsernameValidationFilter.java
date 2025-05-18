package de.yagub.deliverysystem.msuser.validation;

import de.yagub.deliverysystem.msuser.dto.request.RegistrationRequest;
import de.yagub.deliverysystem.msuser.error.InvalidUsernameValidationException;
import de.yagub.deliverysystem.msuser.service.filter.Filter;
import de.yagub.deliverysystem.msuser.service.filter.FilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@Order(1)
public class UsernameValidationFilter implements Filter<RegistrationRequest> {
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_]{3,20}$"); //  Alphanumeric + underscore, 3-20 symbols

    @Override
    public void execute(RegistrationRequest context, FilterChain<RegistrationRequest> chain) {
        String username = context.username();
        if (username == null || !USERNAME_PATTERN.matcher(username).matches()) {
            throw new InvalidUsernameValidationException("Invalid username validation. Pattern : Alphanumeric + underscore, 3-20 symbols ");
        }
        chain.proceed(context);
    }
}
