package de.yagub.deliverysystem.msprocessmanager.error;

import feign.Response;
import feign.error.FeignExceptionConstructor;

public class OrderFailedDependencyException extends OrderProviderException{

    @FeignExceptionConstructor
    public OrderFailedDependencyException(Response response) {
        super(response);
    }
}
