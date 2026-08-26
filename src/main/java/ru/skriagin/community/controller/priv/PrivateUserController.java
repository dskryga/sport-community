package ru.skriagin.community.controller.priv;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skriagin.community.dto.user.UserProfileUpdateDto;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.service.user.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class PrivateUserController {

    private final UserService userService;

    @PatchMapping("/me")
    public UserResponseDto updateProfile(@RequestBody @Valid UserProfileUpdateDto userProfileUpdateDto) {
        log.info("CONTROLLER: получен запрос на обновление профиля текущего пользователя");
        return userService.updateProfile(userProfileUpdateDto);
    }
}
