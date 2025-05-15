package de.yagub.deliverysystem.msprocessmanager.config;

import de.yagub.deliverysystem.msprocessmanager.client.user.UserServiceClient;
import feign.codec.ErrorDecoder;
import feign.error.AnnotationErrorDecoder;
import feign.jackson.JacksonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return AnnotationErrorDecoder
                .builderFor(UserServiceClient.class)
                .withResponseBodyDecoder(new JacksonDecoder())
                .build();
    }


}
