package ru.skriagin.community.dto.event;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventSearchDto {
    private String name;
    private Long categoryId;
    private Long authorId;

    @Min(value = 0, message = "Номер страницы не может быть отрицательным")
    private int page = 0;

    @Min(value = 1, message = "Размер страницы должен быть не менее 1")
    @Max(value = 100, message = "Размер страницы не может превышать 100")
    private int size = 20;

    @DecimalMin(value = "-90.0", message = "Широта должна быть в пределах от -90 до 90")
    @DecimalMax(value = "90.0", message = "Широта должна быть в пределах от -90 до 90")
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "Долгота должна быть в пределах от -180 до 180")
    @DecimalMax(value = "180.0", message = "Долгота должна быть в пределах от -180 до 180")
    private Double longitude;

    @Positive(message = "Радиус поиска должен быть положительным")
    private Double radiusKm;

    @AssertTrue(message = "Для поиска поблизости нужно указать широту, долготу и радиус одновременно")
    public boolean isLocationParamsConsistent() {
        boolean allPresent = latitude != null && longitude != null && radiusKm != null;
        boolean allAbsent = latitude == null && longitude == null && radiusKm == null;
        return allPresent || allAbsent;
    }
}
