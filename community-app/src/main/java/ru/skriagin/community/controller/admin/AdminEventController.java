package ru.skriagin.community.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skriagin.community.dto.event.EventCreateDto;
import ru.skriagin.community.dto.event.EventResponseDto;
import ru.skriagin.community.service.event.EventService;

@RestController
@RequestMapping("/admin/panel/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {

    private final EventService eventService;

    @PutMapping("/{eventId}")
    public EventResponseDto updateEvent(@PathVariable @Min(1) Long eventId,
                                         @RequestBody @Valid EventCreateDto eventUpdateDto) {
        log.info("CONTROLLER: получен запрос на редактирование события {}", eventId);
        return eventService.updateEvent(eventId, eventUpdateDto);
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable @Min(1) Long eventId) {
        log.info("CONTROLLER: получен запрос на удаление события {}", eventId);
        eventService.deleteEvent(eventId);
    }
}
