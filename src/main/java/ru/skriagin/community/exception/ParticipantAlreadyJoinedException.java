package ru.skriagin.community.exception;

public class ParticipantAlreadyJoinedException extends RuntimeException {
    public ParticipantAlreadyJoinedException(Long eventId, Long userId) {
        super("User %d has already joined event %d".formatted(userId, eventId));
    }
}
