package de.yagub.deliverysystem.msprocessmanager.exception;


import de.yagub.deliverysystem.msprocessmanager.exception.msuser.UserLoginException;
import de.yagub.deliverysystem.msprocessmanager.exception.msuser.UserRegistrationException;
import de.yagub.deliverysystem.msprocessmanager.exception.msuser.UserServiceException;
import de.yagub.deliverysystem.msprocessmanager.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ErrorResponse> handleUserServiceException(
            UserServiceException ex, WebRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(ex.getErrorCode(),ex, request);
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }


    private ErrorResponse buildErrorResponse(ErrorCode errorCode, Exception ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return new ErrorResponse(
                errorCode,
                ex.getMessage(),
                path
        );
    }
}
