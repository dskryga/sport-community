package ru.skriagin.community.service.comment;

import ru.skriagin.community.dto.comment.CommentCreateDto;
import ru.skriagin.community.dto.comment.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto createComment(Long eventId, CommentCreateDto commentCreateDto);

    List<CommentResponseDto> getComments(Long eventId);

    void deleteComment(Long commentId);
}
