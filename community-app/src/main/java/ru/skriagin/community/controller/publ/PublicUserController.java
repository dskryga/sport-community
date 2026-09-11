package ru.skriagin.community.controller.publ;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skriagin.community.dto.user.UserCreateDto;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.service.user.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class PublicUserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        log.info("Controller: получен запрос на создание пользователя с именем {}", userCreateDto.getUsername());
        return userService.createUser(userCreateDto);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable @Min(1) Long userId) {
        log.info("Controller: получен запрос на получения пользователя с id {}", userId);
        return userService.getUser(userId);
    }

    @GetMapping("/{userId}/avatar")
    public ResponseEntity<Resource> getAvatar(@PathVariable @Min(1) Long userId) {
        log.info("Controller: получен запрос на получение аватара пользователя с id {}", userId);
        Resource avatar = userService.getAvatar(userId);
        MediaType contentType = MediaTypeFactory.getMediaType(avatar).orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().contentType(contentType).body(avatar);
    }

}
