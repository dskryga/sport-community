package ru.skriagin.community.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.skriagin.community.dto.user.UserCreateDto;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserCreateDto userCreateDto);
    UserResponseDto toResponseDto(User user);
}
