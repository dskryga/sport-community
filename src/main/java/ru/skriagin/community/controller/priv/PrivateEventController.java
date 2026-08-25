package ru.skriagin.community.controller.priv;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skriagin.community.dto.event.EventCreateDto;
import ru.skriagin.community.dto.event.EventResponseDto;
import ru.skriagin.community.service.event.EventService;

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

}
