package ru.skriagin.community.mapper;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skriagin.community.dto.user.UserCreateDto;
import ru.skriagin.community.dto.user.UserProfileUpdateDto;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.model.Role;
import ru.skriagin.community.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "homeLocation", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    @Mapping(target = "gender", ignore = true)
    User toEntity(UserCreateDto userCreateDto);

    default String mapRoleToString(Role role) {
        return role != null ? role.name().replace("ROLE_", "") : null;
    }

    @Mapping(target = "participatingEvents", ignore = true)
    @Mapping(target = "homeLatitude", source = "homeLocation", qualifiedByName = "extractHomeLatitude")
    @Mapping(target = "homeLongitude", source = "homeLocation", qualifiedByName = "extractHomeLongitude")
    UserResponseDto toResponseDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "homeLocation", ignore = true)
    void updateEntityFromDto(UserProfileUpdateDto userProfileUpdateDto, @MappingTarget User user);

    @Named("extractHomeLatitude")
    default Double extractHomeLatitude(Point point) {
        return point != null ? point.getY() : null;
    }

    @Named("extractHomeLongitude")
    default Double extractHomeLongitude(Point point) {
        return point != null ? point.getX() : null;
    }

    default Point mapHomeLocation(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return null;
        }
        return GEOMETRY_FACTORY.createPoint(new Coordinate(longitude, latitude));
    }
}
