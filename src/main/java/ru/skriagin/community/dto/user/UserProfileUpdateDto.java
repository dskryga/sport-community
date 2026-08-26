package ru.skriagin.community.dto.user;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.skriagin.community.model.Gender;

import java.time.LocalDate;

@Getter
@Setter
public class UserProfileUpdateDto {

    @Size(max = 500, message = "Описание не может превышать 500 символов")
    private String description;

    @DecimalMin(value = "-90.0", message = "Широта должна быть в пределах от -90 до 90")
    @DecimalMax(value = "90.0", message = "Широта должна быть в пределах от -90 до 90")
    private Double homeLatitude;

    @DecimalMin(value = "-180.0", message = "Долгота должна быть в пределах от -180 до 180")
    @DecimalMax(value = "180.0", message = "Долгота должна быть в пределах от -180 до 180")
    private Double homeLongitude;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    private Gender gender;

    @AssertTrue(message = "Нужно указать либо обе координаты дома, либо ни одной")
    public boolean isHomeLocationConsistent() {
        return (homeLatitude == null) == (homeLongitude == null);
    }
}
