package ru.skriagin.community.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "Необходимо указать имя пользователя")
    @Size(min = 3, max = 32, message = "Имя пользователя должно быть от 3 до 32 символов")
    private String username;
}
