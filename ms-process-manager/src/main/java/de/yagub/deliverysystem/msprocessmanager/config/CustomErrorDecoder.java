package de.yagub.deliverysystem.msprocessmanager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.yagub.deliverysystem.msprocessmanager.exception.msuser.UserServiceException;
import de.yagub.deliverysystem.msprocessmanager.model.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();
    private final ObjectMapper objectMapper;

    public CustomErrorDecoder() {
        this.objectMapper = Jackson2ObjectMapperBuilder.json().build();
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        if (shouldHandleError(response)) {
            try (InputStream body = response.body().asInputStream()) {
                ErrorResponse errorResponse = objectMapper.readValue(body, ErrorResponse.class);
                log.warn("Received error response from service: {}", errorResponse);
                return createUserServiceException(response, errorResponse);
            } catch (IOException e) {
                log.error("Failed to decode error response", e);
                return new RuntimeException("Failed to process error response", e);
            }
        }
        return defaultDecoder.decode(methodKey, response);
    }

    private boolean shouldHandleError(Response response) {
        return response.body() != null
                && response.status() >= HttpStatus.BAD_REQUEST.value();
    }

    private UserServiceException createUserServiceException(
            Response response,
            ErrorResponse errorResponse
    ) {
        return new UserServiceException(
                errorResponse.message(),
                errorResponse.errorCode(),
                response.status()
        );
    }
}