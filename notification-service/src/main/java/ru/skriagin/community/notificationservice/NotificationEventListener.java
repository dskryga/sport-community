package ru.skriagin.community.notificationservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.skriagin.community.contracts.NotificationEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final JavaMailSender mailSender;

    @KafkaListener(topics = "notification.email.v1", containerFactory = "notificationKafkaListenerContainerFactory")
    public void handle(NotificationEvent event) {
        log.info("Получено событие уведомления {} для {}", event.type(), event.recipientEmail());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.recipientEmail());
        message.setSubject(subjectFor(event));
        message.setText(bodyFor(event));
        mailSender.send(message);

        log.info("Письмо отправлено на {}", event.recipientEmail());
    }

    private String subjectFor(NotificationEvent event) {
        return switch (event.type()) {
            case USER_REGISTERED -> "Добро пожаловать в Sport Community!";
            case EVENT_JOINED -> "Вы записались на событие";
            case EVENT_LEFT -> "Вы отменили участие в событии";
        };
    }

    private String bodyFor(NotificationEvent event) {
        return switch (event.type()) {
            case USER_REGISTERED -> "Привет, %s! Спасибо за регистрацию в Sport Community."
                    .formatted(event.params().get("username"));
            case EVENT_JOINED -> "Вы записались на событие «%s».".formatted(event.params().get("eventName"));
            case EVENT_LEFT -> "Вы отменили участие в событии «%s».".formatted(event.params().get("eventName"));
        };
    }
}
