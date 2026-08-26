package ru.skriagin.community.controller.priv;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skriagin.community.dto.event.EventCreateDto;
import ru.skriagin.community.dto.event.EventResponseDto;
import ru.skriagin.community.dto.event.EventSearchDto;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.service.event.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto createEvent(@RequestBody @Valid EventCreateDto eventCreateDto) {
        log.info("CONTROLLER: получен запрос на создание события {}", eventCreateDto);
        return eventService.createEvent(eventCreateDto);
    }

    @GetMapping
    public List<EventResponseDto> searchEvents(@Valid @ModelAttribute EventSearchDto searchDto) {
        log.info("CONTROLLER: получен запрос на поиск событий {}", searchDto);
        return eventService.searchEvents(searchDto);
    }

    @PostMapping("/{eventId}/participants")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto joinEvent(@PathVariable @Min(1) Long eventId) {
        log.info("CONTROLLER: получен запрос на присоединение к событию {}", eventId);
        return eventService.joinEvent(eventId);
    }

    @DeleteMapping("/{eventId}/participants")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveEvent(@PathVariable @Min(1) Long eventId) {
        log.info("CONTROLLER: получен запрос на выход из события {}", eventId);
        eventService.leaveEvent(eventId);
    }

    @GetMapping("/{eventId}/participants")
    public List<UserResponseDto> getParticipants(@PathVariable @Min(1) Long eventId) {
        log.info("CONTROLLER: получен запрос на список участников события {}", eventId);
        return eventService.getParticipants(eventId);
    }

}
