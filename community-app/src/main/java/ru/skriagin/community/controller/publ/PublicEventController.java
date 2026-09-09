package ru.skriagin.community.controller.publ;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skriagin.community.dto.event.EventCreateDto;
import ru.skriagin.community.dto.event.EventResponseDto;
import ru.skriagin.community.service.event.EventService;

@RequestMapping("/events")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {

    private final EventService eventService;

    @GetMapping("/{eventId}")
    public EventResponseDto getEvent(@PathVariable @Min(1) Long eventId) {
        log.info("CONTROLLER: получен запрос на получение события с id {}", eventId);
        return eventService.getEvent(eventId);
    }
}
