package ru.skriagin.community.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skriagin.community.dto.event.EventResponseDto;
import ru.skriagin.community.model.Gender;
import ru.skriagin.community.model.Role;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private Role role;
    private List<EventResponseDto> participatingEvents;
    private String description;
    private Double homeLatitude;
    private Double homeLongitude;
    private LocalDate birthDate;
    private Gender gender;
}
