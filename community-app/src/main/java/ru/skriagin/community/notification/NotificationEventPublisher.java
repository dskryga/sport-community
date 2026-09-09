package ru.skriagin.community.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.skriagin.community.contracts.NotificationEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventPublisher {

    private static final String TOPIC = "notification.email.v1";

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public void publish(NotificationEvent event) {
        kafkaTemplate.send(TOPIC, event.recipientEmail(), event);
        log.info("Опубликовано событие уведомления {} для {}", event.type(), event.recipientEmail());
    }
}
