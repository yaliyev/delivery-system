package de.yagub.deliverysystem.msprocessmanager.error;

import feign.Response;
import feign.error.FeignExceptionConstructor;

public class WalletFailedDependencyException extends WalletProviderException{

    @FeignExceptionConstructor
    public WalletFailedDependencyException(Response response) {
        super(response);
    }
}
