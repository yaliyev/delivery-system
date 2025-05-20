package de.yagub.deliverysystem.msprocessmanager.config;

import de.yagub.deliverysystem.msprocessmanager.client.order.OrderServiceClient;
import de.yagub.deliverysystem.msprocessmanager.client.wallet.WalletServiceClient;
import feign.Logger;
import feign.codec.ErrorDecoder;
import feign.error.AnnotationErrorDecoder;
import feign.jackson.JacksonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WalletFeignConfig extends GlobalLoggingFeignConfig{
    @Bean
    public ErrorDecoder walletErrorDecoder() {
        return AnnotationErrorDecoder
                .builderFor(WalletServiceClient.class)
                .withResponseBodyDecoder(new JacksonDecoder())
                .build();
    }


}
