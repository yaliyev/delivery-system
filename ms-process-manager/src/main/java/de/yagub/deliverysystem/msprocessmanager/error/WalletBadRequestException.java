package de.yagub.deliverysystem.msprocessmanager.error;

import feign.Response;
import feign.error.FeignExceptionConstructor;

public class WalletBadRequestException extends WalletProviderException{
    @FeignExceptionConstructor
    public WalletBadRequestException(Response response) {
        super(response);
    }
}
