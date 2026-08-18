package ru.skriagin.community.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skriagin.community.dto.user.UserCreateDto;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.exception.EntityNotFoundException;
import ru.skriagin.community.mapper.UserMapper;
import ru.skriagin.community.model.User;
import ru.skriagin.community.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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
        return userMapper.toResponseDto(userRepository.getById(id));
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
}
