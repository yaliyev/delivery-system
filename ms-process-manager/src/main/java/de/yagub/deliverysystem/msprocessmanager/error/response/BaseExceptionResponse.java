package de.yagub.deliverysystem.msprocessmanager.error.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.yagub.deliverysystem.msprocessmanager.error.BaseException;
import feign.Response;
import feign.error.FeignExceptionConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

@Slf4j
public class BaseExceptionResponse extends BaseException {

    private static  final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();


    public BaseExceptionResponse(Response response){
        super(parseErrorResponse(response),response.status());
    }

    private static ErrorResponse parseErrorResponse(Response response) {
        ErrorResponse errorResponse = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "Failed to parse error response",
                UUID.randomUUID(),
                "unknown-path",
                Instant.now());

        if (response.body() != null) {
            try (InputStream body = response.body().asInputStream()) {
                errorResponse = objectMapper.readValue(body, ErrorResponse.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return errorResponse;


    }
}
