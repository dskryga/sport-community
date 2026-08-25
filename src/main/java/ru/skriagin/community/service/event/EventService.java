package ru.skriagin.community.service.event;

import ru.skriagin.community.dto.event.EventCreateDto;
import ru.skriagin.community.dto.event.EventResponseDto;
import ru.skriagin.community.dto.event.EventSearchDto;

import java.util.List;

public interface EventService {
    EventResponseDto createEvent(EventCreateDto eventCreateDto);
    EventResponseDto getEvent(Long id);
    List<EventResponseDto> searchEvents(EventSearchDto searchDto);
}
