package ru.skriagin.community.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skriagin.community.exception.EntityNotFoundException;
import ru.skriagin.community.exception.ParticipantAlreadyJoinedException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(final EntityNotFoundException e) {
        return ApiError.builder().description(e.getMessage()).errorCode(404).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotVaidException(final MethodArgumentNotValidException e){
        return ApiError.builder().description(e.getMessage()).errorCode(400).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleParticipantAlreadyJoinedException(final ParticipantAlreadyJoinedException e) {
        return ApiError.builder().description(e.getMessage()).errorCode(409).build();
    }

}
