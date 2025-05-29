package de.yagub.deliverysystem.msprocessmanager.error;

import feign.Response;
import feign.error.FeignExceptionConstructor;

public class UserBadRequestException extends UserProviderException{

    @FeignExceptionConstructor
    public UserBadRequestException(Response response) {
        super(response);
    }
}
