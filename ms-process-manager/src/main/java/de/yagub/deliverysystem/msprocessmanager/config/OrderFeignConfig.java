package de.yagub.deliverysystem.msprocessmanager.config;

import de.yagub.deliverysystem.msprocessmanager.client.order.OrderServiceClient;
import de.yagub.deliverysystem.msprocessmanager.client.user.UserServiceClient;
import feign.codec.ErrorDecoder;
import feign.error.AnnotationErrorDecoder;
import feign.jackson.JacksonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderFeignConfig {
    @Bean
    public ErrorDecoder orderErrorDecoder() {
        return AnnotationErrorDecoder
                .builderFor(OrderServiceClient.class)
                .withResponseBodyDecoder(new JacksonDecoder())
                .build();
    }
}
