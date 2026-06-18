package ru.skriagin.community.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skriagin.community.dto.event.EventCreateDto;
import ru.skriagin.community.dto.event.EventResponseDto;
import ru.skriagin.community.exception.EntityNotFoundException;
import ru.skriagin.community.mapper.CategoryMapper;
import ru.skriagin.community.mapper.EventMapper;
import ru.skriagin.community.model.Category;
import ru.skriagin.community.model.Event;
import ru.skriagin.community.repository.CategoryRepository;
import ru.skriagin.community.repository.EventRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public EventResponseDto createEvent(EventCreateDto eventCreateDto) {
        Category category = categoryRepository.findById(eventCreateDto.getCategoryId())
                .orElseThrow(() -> {
                    log.info("Категория с id {} не найдена. Событие создать невозможно", eventCreateDto.getCategoryId());
                    return new EntityNotFoundException("Category", eventCreateDto.getCategoryId());
                });

        Event event = eventMapper.toEntity(eventCreateDto, category);
        Event saved = eventRepository.save(event);

        log.info("Событие с id {} сохранено", event.getId());

        return eventMapper.toResponseDto(saved);
    }

    @Override
    public EventResponseDto getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Событие с id {} не найдено", id);
                    return new EntityNotFoundException("Event", id);
                });

        return eventMapper.toResponseDto(event);
    }
}
