package de.yagub.deliverysystem.msprocessmanager.error.handler;


import de.yagub.deliverysystem.msprocessmanager.error.UserServiceException;
import de.yagub.deliverysystem.msprocessmanager.error.response.ErrorCode;
import de.yagub.deliverysystem.msprocessmanager.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ErrorResponse> handleUserServiceException(
            UserServiceException ex, WebRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(ex.getErrorCode(),ex, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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
