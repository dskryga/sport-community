package ru.skriagin.community.controller.publ;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skriagin.community.dto.event.EventCreateDto;
import ru.skriagin.community.dto.event.EventResponseDto;
import ru.skriagin.community.service.event.EventService;

@RequestMapping("/events")
@RestController
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto createEvent(@RequestBody @Valid EventCreateDto eventCreateDto) {
        return eventService.createEvent(eventCreateDto);
    }

    @GetMapping("/{eventId}")
    public EventResponseDto getEvent(@PathVariable @Min(1) Long eventId) {
        return eventService.getEvent(eventId);
    }
}
