package ru.skriagin.community.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skriagin.community.dto.event.EventCreateDto;
import ru.skriagin.community.dto.event.EventResponseDto;
import ru.skriagin.community.dto.event.EventSearchDto;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.exception.EntityNotFoundException;
import ru.skriagin.community.exception.ParticipantAlreadyJoinedException;
import ru.skriagin.community.mapper.CategoryMapper;
import ru.skriagin.community.mapper.EventMapper;
import ru.skriagin.community.mapper.UserMapper;
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
    private final UserMapper userMapper;

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

    @Override
    public UserResponseDto joinEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event", eventId));

        User currentUser = userService.getCurrentUser();

        boolean alreadyJoined = event.getParticipants().stream()
                .anyMatch(participant -> participant.getId().equals(currentUser.getId()));
        if (alreadyJoined) {
            throw new ParticipantAlreadyJoinedException(eventId, currentUser.getId());
        }

        event.getParticipants().add(currentUser);
        eventRepository.save(event);

        log.info("Пользователь {} присоединился к событию {}", currentUser.getId(), eventId);

        return userMapper.toResponseDto(currentUser);
    }

    @Override
    public void leaveEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event", eventId));

        User currentUser = userService.getCurrentUser();

        boolean removed = event.getParticipants()
                .removeIf(participant -> participant.getId().equals(currentUser.getId()));
        if (!removed) {
            throw new EntityNotFoundException("User %d is not a participant of event %d"
                    .formatted(currentUser.getId(), eventId));
        }

        eventRepository.save(event);

        log.info("Пользователь {} покинул событие {}", currentUser.getId(), eventId);
    }

    @Override
    public List<UserResponseDto> getParticipants(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event", eventId));

        return event.getParticipants().stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Override
    public EventResponseDto updateEvent(Long id, EventCreateDto eventUpdateDto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event", id));

        Category category = categoryRepository.findById(eventUpdateDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category", eventUpdateDto.getCategoryId()));

        eventMapper.updateEntityFromDto(eventUpdateDto, event, category);
        Event saved = eventRepository.save(event);

        log.info("Событие с id {} обновлено", id);

        return eventMapper.toResponseDto(saved);
    }

    @Override
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException("Event", id);
        }
        eventRepository.deleteById(id);
        log.info("Событие с id {} удалено", id);
    }
}
