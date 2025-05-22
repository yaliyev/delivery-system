package de.yagub.deliverysystem.msprocessmanager.error;

import feign.Response;
import feign.error.FeignExceptionConstructor;

public class OrderBadRequestException extends OrderProviderException{
    @FeignExceptionConstructor
    public OrderBadRequestException(Response response) {
        super(response);
    }
}
