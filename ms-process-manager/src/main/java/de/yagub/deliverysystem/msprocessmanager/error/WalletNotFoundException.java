package de.yagub.deliverysystem.msprocessmanager.error;

import feign.Response;
import feign.error.FeignExceptionConstructor;

public class WalletNotFoundException extends WalletProviderException{

    @FeignExceptionConstructor
    public WalletNotFoundException(Response response) {
        super(response);
    }
}
