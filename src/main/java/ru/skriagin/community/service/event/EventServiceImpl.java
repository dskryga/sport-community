package ru.skriagin.community.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skriagin.community.dto.event.EventCreateDto;
import ru.skriagin.community.dto.event.EventResponseDto;
import ru.skriagin.community.dto.event.EventSearchDto;
import ru.skriagin.community.exception.EntityNotFoundException;
import ru.skriagin.community.mapper.CategoryMapper;
import ru.skriagin.community.mapper.EventMapper;
import ru.skriagin.community.model.Category;
import ru.skriagin.community.model.Event;
import ru.skriagin.community.model.User;
import ru.skriagin.community.repository.CategoryRepository;
import ru.skriagin.community.repository.EventRepository;
import ru.skriagin.community.repository.specification.EventSpecifications;
import ru.skriagin.community.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserService userService;

    @Override
    public EventResponseDto createEvent(EventCreateDto eventCreateDto) {
        Category category = categoryRepository.findById(eventCreateDto.getCategoryId())
                .orElseThrow(() -> {
                    log.info("Категория с id {} не найдена. Событие создать невозможно", eventCreateDto.getCategoryId());
                    return new EntityNotFoundException("Category", eventCreateDto.getCategoryId());
                });

        User author = userService.getCurrentUser();

        Event event = eventMapper.toEntity(eventCreateDto, category, author);
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

    @Override
    public List<EventResponseDto> searchEvents(EventSearchDto searchDto) {
        log.info("SERVICE: поиск событий по параметрам {}", searchDto);
        return eventRepository.findAll(EventSpecifications.fromSearchParams(searchDto)).stream()
                .map(eventMapper::toResponseDto)
                .toList();
    }
}
