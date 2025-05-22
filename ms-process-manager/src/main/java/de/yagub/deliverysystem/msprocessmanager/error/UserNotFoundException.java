package de.yagub.deliverysystem.msprocessmanager.error;

import feign.Response;
import feign.error.FeignExceptionConstructor;

public class UserNotFoundException extends UserProviderException{
    @FeignExceptionConstructor
    public UserNotFoundException(Response response) {
        super(response);
    }
}
