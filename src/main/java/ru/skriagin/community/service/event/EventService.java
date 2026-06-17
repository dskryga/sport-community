package ru.skriagin.community.service.event;

import ru.skriagin.community.dto.event.EventCreateDto;
import ru.skriagin.community.dto.event.EventResponseDto;

public interface EventService {
    EventResponseDto createEvent(EventCreateDto eventCreateDto);
    EventResponseDto getEvent(Long id);
}
