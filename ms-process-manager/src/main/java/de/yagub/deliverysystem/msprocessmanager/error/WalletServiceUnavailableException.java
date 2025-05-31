package de.yagub.deliverysystem.msprocessmanager.error;

import feign.Response;
import feign.error.FeignExceptionConstructor;

public class WalletServiceUnavailableException extends WalletProviderException{

    @FeignExceptionConstructor
    public WalletServiceUnavailableException(Response response) {
        super(response);
    }
}
