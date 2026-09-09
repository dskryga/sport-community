package ru.skriagin.community.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skriagin.community.dto.user.UserResponseDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long eventId;
    private String text;
    private LocalDateTime createdAt;
    private UserResponseDto author;
}
