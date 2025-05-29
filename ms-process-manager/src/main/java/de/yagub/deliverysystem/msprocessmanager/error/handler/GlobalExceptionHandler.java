package de.yagub.deliverysystem.msprocessmanager.error.handler;


import de.yagub.deliverysystem.msprocessmanager.error.OrderProviderException;
import de.yagub.deliverysystem.msprocessmanager.error.UserProviderException;
import de.yagub.deliverysystem.msprocessmanager.error.WalletProviderException;
import de.yagub.deliverysystem.msprocessmanager.error.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.ConnectException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserProviderException.class)
    public ResponseEntity<ErrorResponse> handleUserProviderException(
            UserProviderException ex, WebRequest request) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getErrorResponse());
    }

    @ExceptionHandler(OrderProviderException.class)
    public ResponseEntity<ErrorResponse> handleOrderProviderException(OrderProviderException ex,WebRequest request){
        return ResponseEntity.status(ex.getStatus()).body(ex.getErrorResponse());
    }

    @ExceptionHandler(WalletProviderException.class)
    public ResponseEntity<ErrorResponse> handleWalletProviderException(WalletProviderException ex,WebRequest request){
        return ResponseEntity.status(ex.getStatus()).body(ex.getErrorResponse());
    }

    @ExceptionHandler(ConnectException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleConnectException(ConnectException ex,WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse(
                         "CONNECTION_REFUSED",
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                );
        return errorResponse;
    }

//    private void logError(ErrorResponse errorResponse){
//        log.error(errorResponse.toString());
//    }

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
