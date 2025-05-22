package de.yagub.deliverysystem.msuser.service.filter.impl;

import de.yagub.deliverysystem.msuser.error.InvalidUsernameValidationException;
import de.yagub.deliverysystem.msuser.model.FilterTarget;
import de.yagub.deliverysystem.msuser.model.enums.FilterId;
import de.yagub.deliverysystem.msuser.service.filter.Filter;
import de.yagub.deliverysystem.msuser.service.filter.FilterChainBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@Order(1)
public class UsernameValidationFilter implements Filter{
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_]{3,20}$"); //  Alphanumeric + underscore, 3-20 symbols

    @Override
    public void execute(FilterTarget filterTarget) {
        String username = filterTarget.getRegistrationRequest().username();
        if (username == null || !USERNAME_PATTERN.matcher(username).matches()) {
            throw new InvalidUsernameValidationException("Invalid username validation. Pattern : Alphanumeric + underscore, 3-20 symbols ");
        }
    }

    @Override
    public FilterId id() {
        return FilterId.USERNAME_VALIDATION_FILTER;
    }
}
