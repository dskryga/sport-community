package ru.skriagin.community.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.skriagin.community.dto.comment.CommentCreateDto;
import ru.skriagin.community.dto.comment.CommentResponseDto;
import ru.skriagin.community.model.Comment;
import ru.skriagin.community.model.Event;
import ru.skriagin.community.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "event", expression = "java(event)")
    @Mapping(target = "author", expression = "java(author)")
    Comment toEntity(CommentCreateDto commentCreateDto, @Context Event event, @Context User author);

    @Mapping(target = "eventId", source = "event.id")
    CommentResponseDto toResponseDto(Comment comment);
}
