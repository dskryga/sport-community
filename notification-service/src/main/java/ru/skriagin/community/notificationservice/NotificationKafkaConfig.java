package ru.skriagin.community.notificationservice;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.skriagin.community.contracts.NotificationEvent;

import java.util.Map;

@Configuration
@EnableKafka
public class NotificationKafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, NotificationEvent> notificationConsumerFactory() {
        Map<String, Object> config = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, "notification-service",
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        );
        JsonDeserializer<NotificationEvent> valueDeserializer = new JsonDeserializer<>(NotificationEvent.class);
        valueDeserializer.addTrustedPackages("ru.skriagin.community.contracts");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), valueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificationEvent> notificationKafkaListenerContainerFactory(
            ConsumerFactory<String, NotificationEvent> notificationConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, NotificationEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(notificationConsumerFactory);
        return factory;
    }
}
