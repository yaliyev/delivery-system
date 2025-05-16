package de.yagub.deliverysystem.msprocessmanager.error.handler;


import de.yagub.deliverysystem.msprocessmanager.error.OrderProviderException;
import de.yagub.deliverysystem.msprocessmanager.error.UserProviderException;
import de.yagub.deliverysystem.msprocessmanager.error.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserProviderException.class)
    public ResponseEntity<ErrorResponse> handleUserProviderException(
            UserProviderException ex, WebRequest request) {
        logError(ex.getErrorResponse());
        return ResponseEntity.status(ex.getStatus()).body(ex.getErrorResponse());
    }

    @ExceptionHandler(OrderProviderException.class)
    public ResponseEntity<ErrorResponse> handleOrderProviderException(OrderProviderException ex,WebRequest request){
        logError(ex.getErrorResponse());
        return ResponseEntity.status(ex.getStatus()).body(ex.getErrorResponse());
    }

    private void logError(ErrorResponse errorResponse){
        log.error(errorResponse.toString());
    }

//    private ErrorResponse buildErrorResponse(String errorCode, Exception ex, WebRequest request) {
//        String path = request.getDescription(false).replace("uri=", "");
//        ErrorResponse errorResponse = new ErrorResponse(
//                errorCode,
//                ex.getMessage(),
//                path
//        );
//
//        return errorResponse;
//    }
}
