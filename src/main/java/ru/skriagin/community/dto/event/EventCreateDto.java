package ru.skriagin.community.dto.event;

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
    private Long categoryId;
    private String description;
    private LocalDateTime dateTime;
    private Double latitude;
    private Double longitude;
}
