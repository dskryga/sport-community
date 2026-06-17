package ru.skriagin.community.service.user;

import ru.skriagin.community.dto.user.UserCreateDto;
import ru.skriagin.community.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserCreateDto userCreateDto);

    UserResponseDto getUser(Long id);

}
