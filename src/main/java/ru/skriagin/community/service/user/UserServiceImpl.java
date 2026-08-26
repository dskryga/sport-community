package ru.skriagin.community.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skriagin.community.dto.user.UserCreateDto;
import ru.skriagin.community.dto.user.UserProfileUpdateDto;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.exception.EntityNotFoundException;
import ru.skriagin.community.mapper.EventMapper;
import ru.skriagin.community.mapper.UserMapper;
import ru.skriagin.community.model.Role;
import ru.skriagin.community.model.User;
import ru.skriagin.community.repository.EventRepository;
import ru.skriagin.community.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        if (userRepository.findByUsername(userCreateDto.getUsername()).isPresent()) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        User createdUser = userMapper.toEntity(userCreateDto);

        createdUser.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));

        createdUser = userRepository.save(createdUser);
        log.info("SERVICE: Пользователь с id {} и именем {} создан", createdUser.getId(), createdUser.getUsername());
        return userMapper.toResponseDto(createdUser);
    }

    @Override
    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("SERVICE: Пользователь с id {} не найден", id);
                    return new EntityNotFoundException("User", id);
                });

        return toResponseDtoWithEvents(user);
    }

    private UserResponseDto toResponseDtoWithEvents(User user) {
        UserResponseDto responseDto = userMapper.toResponseDto(user);
        responseDto.setParticipatingEvents(
                eventRepository.findByParticipants_Id(user.getId()).stream()
                        .map(eventMapper::toResponseDto)
                        .toList()
        );

        return responseDto;
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        throw new RuntimeException("Cant get current user");
    }

    @Override
    public UserResponseDto changeUserRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("SERVICE: Пользователь с id {} не найден", id);
                    return new EntityNotFoundException("User", id);
                });

        user.setRole(role);
        User saved = userRepository.save(user);

        log.info("Роль пользователя с id {} изменена на {}", id, role);

        return userMapper.toResponseDto(saved);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User", id);
        }
        userRepository.deleteById(id);
        log.info("SERVICE: Пользователь с id {} удалён", id);
    }

    @Override
    public UserResponseDto updateProfile(UserProfileUpdateDto userProfileUpdateDto) {
        User currentUser = getCurrentUser();

        userMapper.updateEntityFromDto(userProfileUpdateDto, currentUser);
        if (userProfileUpdateDto.getHomeLatitude() != null && userProfileUpdateDto.getHomeLongitude() != null) {
            currentUser.setHomeLocation(userMapper.mapHomeLocation(
                    userProfileUpdateDto.getHomeLatitude(),
                    userProfileUpdateDto.getHomeLongitude()
            ));
        }

        User saved = userRepository.save(currentUser);

        log.info("SERVICE: Профиль пользователя с id {} обновлён", saved.getId());

        return toResponseDtoWithEvents(saved);
    }
}
