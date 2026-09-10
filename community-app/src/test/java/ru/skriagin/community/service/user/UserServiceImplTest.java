package ru.skriagin.community.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skriagin.community.dto.user.UserCreateDto;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.exception.EntityNotFoundException;
import ru.skriagin.community.mapper.EventMapper;
import ru.skriagin.community.mapper.UserMapper;
import ru.skriagin.community.model.User;
import ru.skriagin.community.notification.NotificationEventPublisher;
import ru.skriagin.community.repository.EventRepository;
import ru.skriagin.community.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventMapper eventMapper;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private NotificationEventPublisher notificationEventPublisher;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_savesNewUserAndPublishesNotification() {
        UserCreateDto dto = new UserCreateDto("newuser", "password123");
        User mappedUser = new User();
        mappedUser.setUsername("newuser");
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setUsername("newuser");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userMapper.toEntity(dto)).thenReturn(mappedUser);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(mappedUser)).thenReturn(savedUser);
        when(userMapper.toResponseDto(savedUser)).thenReturn(responseDto);

        UserResponseDto result = userService.createUser(dto);

        assertThat(result.getUsername()).isEqualTo("newuser");
        verify(notificationEventPublisher).publish(any());
    }

    @Test
    void createUser_duplicateUsername_throwsAndDoesNotSave() {
        UserCreateDto dto = new UserCreateDto("existing", "password123");
        when(userRepository.findByUsername("existing")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(RuntimeException.class);

        verify(userRepository, never()).save(any());
        verifyNoInteractions(notificationEventPublisher);
    }

    @Test
    void getUser_notFound_throwsEntityNotFoundException() {
        when(userRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(42L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
