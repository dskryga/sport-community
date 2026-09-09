package ru.skriagin.community.controller.priv;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skriagin.community.dto.comment.CommentCreateDto;
import ru.skriagin.community.dto.comment.CommentResponseDto;
import ru.skriagin.community.service.comment.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@PathVariable @Min(1) Long eventId,
                                             @RequestBody @Valid CommentCreateDto commentCreateDto) {
        log.info("CONTROLLER: получен запрос на добавление комментария к событию {}", eventId);
        return commentService.createComment(eventId, commentCreateDto);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentResponseDto> getComments(@PathVariable @Min(1) Long eventId) {
        log.info("CONTROLLER: получен запрос на список комментариев события {}", eventId);
        return commentService.getComments(eventId);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Min(1) Long commentId) {
        log.info("CONTROLLER: получен запрос на удаление комментария {}", commentId);
        commentService.deleteComment(commentId);
    }
}
