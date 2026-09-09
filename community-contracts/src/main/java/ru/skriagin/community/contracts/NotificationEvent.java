package ru.skriagin.community.contracts;

import java.util.Map;
import java.util.UUID;

public record NotificationEvent(
        UUID eventId,
        NotificationType type,
        String recipientEmail,
        Map<String, String> params
) {
}
