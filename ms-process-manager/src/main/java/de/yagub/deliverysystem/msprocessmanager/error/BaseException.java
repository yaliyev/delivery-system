package de.yagub.deliverysystem.msprocessmanager.error;

import de.yagub.deliverysystem.msprocessmanager.error.response.ErrorResponse;
import feign.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
//@EqualsAndHashCode(callSuper = false)
public class BaseException extends RuntimeException{
    private ErrorResponse errorResponse;
    private final int status;

    public BaseException(ErrorResponse errorResponse,int status){
        super(errorResponse.message());
        this.errorResponse = errorResponse;
        this.status = status;
    }

}
