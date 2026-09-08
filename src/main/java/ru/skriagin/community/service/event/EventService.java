package ru.skriagin.community.service.event;

import ru.skriagin.community.dto.common.PageResponseDto;
import ru.skriagin.community.dto.event.EventCreateDto;
import ru.skriagin.community.dto.event.EventResponseDto;
import ru.skriagin.community.dto.event.EventSearchDto;
import ru.skriagin.community.dto.user.UserResponseDto;

import java.util.List;

public interface EventService {
    EventResponseDto createEvent(EventCreateDto eventCreateDto);
    EventResponseDto getEvent(Long id);
    PageResponseDto<EventResponseDto> searchEvents(EventSearchDto searchDto);
    UserResponseDto joinEvent(Long eventId);
    void leaveEvent(Long eventId);
    List<UserResponseDto> getParticipants(Long eventId);
    EventResponseDto updateEvent(Long id, EventCreateDto eventUpdateDto);
    void deleteEvent(Long id);
}
