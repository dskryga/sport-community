package ru.skriagin.community.dto.event;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateDto {
    @NotNull(message = "Требуется ID категории")
    private Long categoryId;
    @NotBlank(message = "Требуется название события")
    @Size(max = 150, message = "Название не может превышать 150 символов")
    private String name;
    @Size(max = 1000, message = "Описание не может превышать 1000 символов")
    private String description;
    @NotNull(message = "У события должна быть дата")
    @Future(message = "Дата события должна быть в будущем")
    private LocalDateTime dateTime;
    @NotNull(message = "Необходимо указать координаты: широта")
    @DecimalMin(value = "-90.0", message = "Широта должна быть в пределах от -90 до 90")
    @DecimalMax(value = "90.0", message = "Широта должна быть в пределах от -90 до 90")
    private Double latitude;
    @NotNull(message = "Необходимо указать координаты: долгота")
    @DecimalMin(value = "-180.0", message = "Долгота должна быть в пределах от -180 до 180")
    @DecimalMax(value = "180.0", message = "Долгота должна быть в пределах от -180 до 180")
    private Double longitude;
}
