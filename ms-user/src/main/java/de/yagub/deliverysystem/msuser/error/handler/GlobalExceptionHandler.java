package de.yagub.deliverysystem.msuser.error.handler;

import de.yagub.deliverysystem.msuser.error.InvalidPasswordValidationException;
import de.yagub.deliverysystem.msuser.error.InvalidUserCredentialsException;
import de.yagub.deliverysystem.msuser.error.InvalidUsernameValidationException;
import de.yagub.deliverysystem.msuser.error.UsernameAlreadyExistsException;
import de.yagub.deliverysystem.msuser.error.response.ErrorCode;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import de.yagub.deliverysystem.msuser.error.response.ErrorResponse;

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

    @ExceptionHandler(InvalidUsernameValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidUsernameValidationException(
            InvalidUsernameValidationException ex,
            WebRequest request) {

        ErrorResponse response = new ErrorResponse(
                ErrorCode.INVALID_USERNAME_VALIDATION,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return response;
    }

    @ExceptionHandler(InvalidPasswordValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidPasswordValidationException(
            InvalidPasswordValidationException ex,
            WebRequest request) {

        ErrorResponse response = new ErrorResponse(
                ErrorCode.INVALID_PASSWORD_VALIDATION,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return response;
    }

}
