package de.yagub.deliverysystem.msprocessmanager.error;

import feign.Response;
import feign.error.FeignExceptionConstructor;

public class UserFailedDependencyException extends UserProviderException{

    @FeignExceptionConstructor
    public UserFailedDependencyException(Response response) {
        super(response);
    }
}
