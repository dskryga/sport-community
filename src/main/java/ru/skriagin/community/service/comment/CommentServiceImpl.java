package ru.skriagin.community.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.skriagin.community.dto.comment.CommentCreateDto;
import ru.skriagin.community.dto.comment.CommentResponseDto;
import ru.skriagin.community.exception.EntityNotFoundException;
import ru.skriagin.community.mapper.CommentMapper;
import ru.skriagin.community.model.Comment;
import ru.skriagin.community.model.Event;
import ru.skriagin.community.model.Role;
import ru.skriagin.community.model.User;
import ru.skriagin.community.repository.CommentRepository;
import ru.skriagin.community.repository.EventRepository;
import ru.skriagin.community.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;

    @Override
    public CommentResponseDto createComment(Long eventId, CommentCreateDto commentCreateDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event", eventId));

        User author = userService.getCurrentUser();

        Comment comment = commentMapper.toEntity(commentCreateDto, event, author);
        Comment saved = commentRepository.save(comment);

        log.info("Комментарий с id {} добавлен к событию {}", saved.getId(), eventId);

        return commentMapper.toResponseDto(saved);
    }

    @Override
    public List<CommentResponseDto> getComments(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Event", eventId);
        }

        return commentRepository.findByEvent_IdOrderByCreatedAtAsc(eventId).stream()
                .map(commentMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment", commentId));

        User currentUser = userService.getCurrentUser();

        boolean isCommentAuthor = comment.getAuthor().getId().equals(currentUser.getId());
        boolean isEventAuthor = comment.getEvent().getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;

        if (!isCommentAuthor && !isEventAuthor && !isAdmin) {
            throw new AccessDeniedException("Нет прав доступа для удаления комментария %d".formatted(commentId));
        }

        commentRepository.deleteById(commentId);

        log.info("Комментарий с id {} удалён пользователем {}", commentId, currentUser.getId());
    }
}
