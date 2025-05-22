package de.yagub.deliverysystem.msorder.error.handler;

import de.yagub.deliverysystem.msorder.error.CustomerNotFoundException;
import de.yagub.deliverysystem.msorder.error.OrderNotFoundException;
import de.yagub.deliverysystem.msorder.error.response.ErrorCode;
import de.yagub.deliverysystem.msorder.error.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request){
        ErrorResponse response = new ErrorResponse(
                ErrorCode.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return response;
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCustomerNotFoundException(CustomerNotFoundException ex, WebRequest request){
        ErrorResponse response = new ErrorResponse(
                ErrorCode.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return response;
    }

}
