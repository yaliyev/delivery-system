package de.yagub.deliverysystem.msprocessmanager.exception.msuser;

import de.yagub.deliverysystem.msprocessmanager.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UserServiceException extends RuntimeException {
    private final ErrorCode errorCode;
    private final int httpStatus;

    public UserServiceException(String message, ErrorCode errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }


}
