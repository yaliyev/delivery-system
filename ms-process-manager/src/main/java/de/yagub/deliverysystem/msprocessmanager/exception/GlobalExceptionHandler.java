package de.yagub.deliverysystem.msprocessmanager.exception;


import de.yagub.deliverysystem.msprocessmanager.exception.msuser.UserLoginException;
import de.yagub.deliverysystem.msprocessmanager.exception.msuser.UserRegistrationException;
import de.yagub.deliverysystem.msprocessmanager.exception.msuser.UserServiceException;
import de.yagub.deliverysystem.msprocessmanager.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserServiceException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleUserServiceException(
            UserServiceException ex, WebRequest request) {
        return buildErrorResponse(ex.getErrorCode(),ex, request);
    }


    private ErrorResponse buildErrorResponse(ErrorCode errorCode,Exception ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return new ErrorResponse(
                errorCode,
                ex.getMessage(),
                path
        );
    }
}
