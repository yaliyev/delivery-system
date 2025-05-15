package de.yagub.deliverysystem.msprocessmanager.error;

import de.yagub.deliverysystem.msprocessmanager.error.response.BaseExceptionResponse;

import de.yagub.deliverysystem.msprocessmanager.error.response.ErrorResponse;
import feign.Response;

import feign.error.FeignExceptionConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class UserProviderException extends BaseExceptionResponse {


    @FeignExceptionConstructor
    public UserProviderException(Response response){
        super(response);
    }
}
