package ru.skriagin.community.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.skriagin.community.dto.user.UserCreateDto;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.model.Role;
import ru.skriagin.community.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserCreateDto userCreateDto);

    default String mapRoleToString(Role role) {
        return role != null ? role.name().replace("ROLE_", "") : null;
    }

    UserResponseDto toResponseDto(User user);
}
