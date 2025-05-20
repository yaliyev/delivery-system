package de.yagub.deliverysystem.msprocessmanager.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class GlobalLoggingFeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
