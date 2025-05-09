package de.yagub.deliverysystem.msprocessmanager.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.yagub.deliverysystem.msprocessmanager.error.response.ErrorCode;
import de.yagub.deliverysystem.msprocessmanager.model.ErrorResponse;
import feign.Response;
import feign.error.FeignExceptionConstructor;
import lombok.Getter;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserServiceException extends RuntimeException {
    private final ErrorCode errorCode;
    private final int httpStatus;
    private final UUID uuid;
    private final String path;

    // Constructor for manual exception creation
    public UserServiceException(String message, ErrorCode errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.uuid = UUID.randomUUID();
        this.path = ""; // Set actual path if available
    }

    // Constructor for Feign integration
    @FeignExceptionConstructor
    public UserServiceException(Response response) {
        super(resolveMessage(response));
        ErrorResponse errorResponse = parseErrorResponse(response);
        this.errorCode = errorResponse.errorCode();
        this.httpStatus = response.status();
        this.uuid = errorResponse.uuid();
        this.path = errorResponse.path();
    }

    private static String resolveMessage(Response response) {
        ErrorResponse errorResponse = parseErrorResponse(response);
        return errorResponse.message() != null ?
                errorResponse.message() :
                "User Service Error - Status: " + response.status();
    }

    private static ErrorResponse parseErrorResponse(Response response) {
        try {
            if (response.body() != null) {
                return new ObjectMapper().readValue(
                        response.body().asInputStream(),
                        ErrorResponse.class
                );
            }
        } catch (IOException e) {
            // Log deserialization error if needed
        }

        // Fallback to empty response
        return new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "Failed to parse error response",
                UUID.randomUUID(),
                "unknown-path",
                Instant.now()
        );
    }
}
