package ru.skriagin.community.controller.publ;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skriagin.community.dto.user.UserCreateDto;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.service.user.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PublicUserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable @Min(1) Long userId) {
        return userService.getUser(userId);
    }

}
