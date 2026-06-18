package ru.skriagin.community.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        User createdUser = userMapper.toEntity(userCreateDto);
        createdUser = userRepository.save(createdUser);
        log.info("SERVICE: Пользователь с id {} и именем {} создан", createdUser.getId(), createdUser.getUsername());
        return userMapper.toResponseDto(createdUser);
    }

    @Override
    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("SERVICE: Пользователь с id {} не найден", id);
                    return new  EntityNotFoundException("User", id);
                });
        return userMapper.toResponseDto(userRepository.getById(id));
    }
}
