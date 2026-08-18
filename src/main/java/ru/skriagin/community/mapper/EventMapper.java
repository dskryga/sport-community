package ru.skriagin.community.mapper;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mapstruct.*;
import ru.skriagin.community.dto.event.EventCreateDto;
import ru.skriagin.community.dto.event.EventResponseDto;
import ru.skriagin.community.model.Category;
import ru.skriagin.community.model.Event;
import ru.skriagin.community.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
uses = {CategoryMapper.class, UserMapper.class})
public abstract class EventMapper {
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", expression = "java(category)")
    @Mapping(target = "author", expression = "java(author)")
    @Mapping(target = "location", source = "eventCreateDto", qualifiedByName = "mapLocation")
    public abstract Event toEntity(EventCreateDto eventCreateDto, @Context Category category, @Context User author);

    @Mapping(target = "category", source = "category")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "latitude", source = "location", qualifiedByName = "extractLatitude")
    @Mapping(target = "longitude", source = "location", qualifiedByName = "extractLongitude")
    public abstract EventResponseDto toResponseDto(Event event);

    @Named("extractLongitude")
    protected Double extractLongitude(Point point) {
        return point != null ? point.getX() : null;
    }

    @Named("extractLatitude")
    protected Double extractLatitude(Point point) {
        return point != null ? point.getY() : null;
    }

    @Named("mapLocation")
    protected Point mapLocation(EventCreateDto dto) {
        if (dto.getLatitude() == null || dto.getLongitude() == null) {
            return null;
        }
        return GEOMETRY_FACTORY.createPoint(
                new Coordinate(dto.getLongitude(), dto.getLatitude())
        );
    }
    @Deprecated
    @Named("mapCategory")
    protected Category mapCategory(Long categoryId, @Context Category categoryParam) {
        return categoryParam;
    }

}
