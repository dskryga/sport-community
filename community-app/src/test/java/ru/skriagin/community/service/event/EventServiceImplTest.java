package ru.skriagin.community.service.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import ru.skriagin.community.contracts.NotificationEvent;
import ru.skriagin.community.contracts.NotificationType;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.exception.EntityNotFoundException;
import ru.skriagin.community.exception.ParticipantAlreadyJoinedException;
import ru.skriagin.community.mapper.CategoryMapper;
import ru.skriagin.community.mapper.EventMapper;
import ru.skriagin.community.mapper.UserMapper;
import ru.skriagin.community.model.Event;
import ru.skriagin.community.model.User;
import ru.skriagin.community.notification.NotificationEventPublisher;
import ru.skriagin.community.repository.CategoryRepository;
import ru.skriagin.community.repository.EventRepository;
import ru.skriagin.community.service.user.UserService;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventMapper eventMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private NotificationEventPublisher notificationEventPublisher;

    @InjectMocks
    private EventServiceImpl eventService;

    private User currentUser;
    private Event event;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setId(10L);
        currentUser.setUsername("participant");

        event = new Event();
        event.setId(5L);
        event.setName("Football");
        event.setParticipants(new HashSet<>());
    }

    @Test
    void joinEvent_addsParticipantAndPublishesNotification() {
        when(eventRepository.findById(5L)).thenReturn(Optional.of(event));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(userMapper.toResponseDto(currentUser)).thenReturn(new UserResponseDto());

        eventService.joinEvent(5L);

        assertThat(event.getParticipants()).contains(currentUser);

        ArgumentCaptor<NotificationEvent> captor = ArgumentCaptor.forClass(NotificationEvent.class);
        verify(notificationEventPublisher).publish(captor.capture());
        assertThat(captor.getValue().type()).isEqualTo(NotificationType.EVENT_JOINED);
    }

    @Test
    void joinEvent_alreadyJoined_throwsAndDoesNotPublish() {
        event.getParticipants().add(currentUser);
        when(eventRepository.findById(5L)).thenReturn(Optional.of(event));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        assertThatThrownBy(() -> eventService.joinEvent(5L))
                .isInstanceOf(ParticipantAlreadyJoinedException.class);

        verifyNoInteractions(notificationEventPublisher);
    }

    @Test
    void leaveEvent_removesParticipantAndPublishesNotification() {
        event.getParticipants().add(currentUser);
        when(eventRepository.findById(5L)).thenReturn(Optional.of(event));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        eventService.leaveEvent(5L);

        assertThat(event.getParticipants()).doesNotContain(currentUser);

        ArgumentCaptor<NotificationEvent> captor = ArgumentCaptor.forClass(NotificationEvent.class);
        verify(notificationEventPublisher).publish(captor.capture());
        assertThat(captor.getValue().type()).isEqualTo(NotificationType.EVENT_LEFT);
    }

    @Test
    void leaveEvent_notParticipant_throwsEntityNotFoundException() {
        when(eventRepository.findById(5L)).thenReturn(Optional.of(event));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        assertThatThrownBy(() -> eventService.leaveEvent(5L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
