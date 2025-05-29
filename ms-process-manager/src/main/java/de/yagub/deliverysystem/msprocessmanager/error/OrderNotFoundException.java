package de.yagub.deliverysystem.msprocessmanager.error;

import feign.Response;
import feign.error.FeignExceptionConstructor;

public class OrderNotFoundException extends OrderProviderException{
    @FeignExceptionConstructor
    public OrderNotFoundException(Response response) {
        super(response);
    }
}
