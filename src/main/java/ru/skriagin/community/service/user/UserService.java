package ru.skriagin.community.service.user;

import ru.skriagin.community.dto.user.UserCreateDto;
import ru.skriagin.community.dto.user.UserProfileUpdateDto;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.model.Role;
import ru.skriagin.community.model.User;

public interface UserService {
    UserResponseDto createUser(UserCreateDto userCreateDto);

    UserResponseDto getUser(Long id);

    User getCurrentUser();

    UserResponseDto changeUserRole(Long id, Role role);

    void deleteUser(Long id);

    UserResponseDto updateProfile(UserProfileUpdateDto userProfileUpdateDto);

}
