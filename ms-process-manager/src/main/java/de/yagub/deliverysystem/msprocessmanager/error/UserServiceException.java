package de.yagub.deliverysystem.msprocessmanager.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.yagub.deliverysystem.msprocessmanager.error.response.ErrorCode;
import de.yagub.deliverysystem.msprocessmanager.model.ErrorResponse;
import feign.Response;
import feign.error.FeignExceptionConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Getter
public class UserServiceException extends RuntimeException {

    private   final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final ErrorCode errorCode;
    private final String message;
    private final int httpStatus;
    private final UUID uuid;
    private final String path;


    public UserServiceException(String message, ErrorCode errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
        this.uuid = UUID.randomUUID();
        this.path = ""; // Set actual path if available
    }

    @FeignExceptionConstructor
    public UserServiceException(Response response) {
        super(response.toString());
        ErrorResponse errorResponse = parseErrorResponse(response);
        this.errorCode = errorResponse.errorCode();
        this.message = errorResponse.message();
        this.httpStatus = response.status();
        this.uuid = errorResponse.uuid();
        this.path = errorResponse.path();
    }
    private ErrorResponse parseErrorResponse(Response response) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "Failed to parse error response",
                UUID.randomUUID(),
                "unknown-path",
                Instant.now());

        if (response.body() != null) {
            try (InputStream body = response.body().asInputStream()) {
                errorResponse = objectMapper.readValue(body, ErrorResponse.class);
                log.warn("Received error response from service: {}", errorResponse);
            } catch (IOException e) {
                log.error("Failed to decode error response", e);
            }

        }

        return errorResponse;


    }
}
