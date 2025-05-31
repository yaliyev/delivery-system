package de.yagub.deliverysystem.msprocessmanager.error;

import feign.Response;
import feign.error.FeignExceptionConstructor;

public class OrderServiceUnavailableException extends OrderProviderException{

    @FeignExceptionConstructor
    public OrderServiceUnavailableException(Response response) {
        super(response);
    }
}
