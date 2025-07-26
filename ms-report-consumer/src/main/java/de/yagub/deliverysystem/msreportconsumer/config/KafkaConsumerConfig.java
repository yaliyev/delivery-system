package de.yagub.deliverysystem.msreportconsumer.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

//@EnableKafka
//@Configuration
public class KafkaConsumerConfig {


//    @Bean
//    public ConsumerFactory<String, Object> consumerFactory(KafkaProperties properties) {
//        // buildConsumerProperties() gathers all your settings from application.yaml
//        return new DefaultKafkaConsumerFactory<>(properties.buildConsumerProperties(null));
//    }
//
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
//            ConsumerFactory<String, Object> consumerFactory) {
//
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory);
//        return factory;
//    }
}