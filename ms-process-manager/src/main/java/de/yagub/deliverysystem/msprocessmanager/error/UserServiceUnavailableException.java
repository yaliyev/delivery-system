package de.yagub.deliverysystem.msprocessmanager.error;

import feign.Response;
import feign.error.FeignExceptionConstructor;

public class UserServiceUnavailableException extends UserProviderException{

    @FeignExceptionConstructor
    public UserServiceUnavailableException(Response response) {
        super(response);
    }
}
