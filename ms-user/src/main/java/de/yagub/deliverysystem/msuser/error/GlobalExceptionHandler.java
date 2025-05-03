package de.yagub.deliverysystem.msuser.error;

import de.yagub.deliverysystem.msuser.error.customexceptions.InvalidUserCredentialsException;
import de.yagub.deliverysystem.msuser.error.customexceptions.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import de.yagub.deliverysystem.msuser.dto.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUsernameAlreadyExistsException(
            UsernameAlreadyExistsException ex,
            WebRequest request) {

        ErrorResponse response = new ErrorResponse(
                ErrorCode.USERNAME_ALREADY_EXISTS,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return response;
    }

    @ExceptionHandler(InvalidUserCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidUserCredentialsException(
            InvalidUserCredentialsException ex,
            WebRequest request) {

        ErrorResponse response = new ErrorResponse(
                ErrorCode.UNAUTHORIZED,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return response;
    }

}
