package ru.skriagin.community.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skriagin.community.dto.user.UserResponseDto;
import ru.skriagin.community.dto.user.UserRoleUpdateDto;
import ru.skriagin.community.service.user.UserService;

@RestController
@RequestMapping("/admin/panel/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final UserService userService;

    @PatchMapping("/{userId}/role")
    public UserResponseDto changeUserRole(@PathVariable @Min(1) Long userId,
                                           @RequestBody @Valid UserRoleUpdateDto userRoleUpdateDto) {
        log.info("CONTROLLER: получен запрос на изменение роли пользователя {}", userId);
        return userService.changeUserRole(userId, userRoleUpdateDto.getRole());
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Min(1) Long userId) {
        log.info("CONTROLLER: получен запрос на удаление пользователя {}", userId);
        userService.deleteUser(userId);
    }
}
