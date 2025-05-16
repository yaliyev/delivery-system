package de.yagub.deliverysystem.msprocessmanager.error;

import de.yagub.deliverysystem.msprocessmanager.error.response.BaseExceptionResponse;
import feign.Response;
import feign.error.FeignExceptionConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class OrderProviderException extends BaseExceptionResponse {


    @FeignExceptionConstructor
    public OrderProviderException(Response response){
        super(response);
    }
}
