package de.yagub.deliverysystem.msuser.validation;

import de.yagub.deliverysystem.msuser.dto.request.RegistrationRequest;
import de.yagub.deliverysystem.msuser.error.InvalidPasswordValidationException;
import de.yagub.deliverysystem.msuser.service.filter.Filter;
import de.yagub.deliverysystem.msuser.service.filter.FilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@Order(2)
public class PasswordStrengthFilter implements Filter<RegistrationRequest> {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
    );

    @Override
    public void execute(RegistrationRequest context, FilterChain<RegistrationRequest> chain) {
        String password = context.password();
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new InvalidPasswordValidationException(
                    "Password must be 8+ chars with 1 uppercase, 1 lowercase, 1 digit, and 1 special character (@#$%^&+=!)."
            );
        }
        chain.proceed(context);
    }
}
