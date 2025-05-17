package de.yagub.deliverysystem.mswallet.error.handler;

import de.yagub.deliverysystem.mswallet.error.*;
import de.yagub.deliverysystem.mswallet.error.response.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import de.yagub.deliverysystem.mswallet.error.response.ErrorResponse;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleInsufficientFundsException(InsufficientFundsException ex, WebRequest request){
        ErrorResponse response = new ErrorResponse(
                ErrorCode.INSUFFICIENT_FUNDS,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return response;
    }

    @ExceptionHandler(WalletAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleWalletAlreadyExistsException(WalletAlreadyExistsException ex, WebRequest request){
        ErrorResponse response = new ErrorResponse(
                ErrorCode.ALREADY_EXISTS,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return response;
    }

    @ExceptionHandler(WalletInactiveException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleWalletInactiveException(WalletInactiveException ex, WebRequest request){
        ErrorResponse response = new ErrorResponse(
                ErrorCode.WALLET_INACTIVE,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return response;
    }

    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWalletNotFoundException(WalletNotFoundException ex, WebRequest request){
        ErrorResponse response = new ErrorResponse(
                ErrorCode.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return response;
    }

    @ExceptionHandler(WalletUpdateFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleWalletUpdateFailedException(WalletUpdateFailedException ex, WebRequest request){
        ErrorResponse response = new ErrorResponse(
                ErrorCode.WALLET_UPDATE_FAILED,
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return response;
    }












}
