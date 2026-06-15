package ru.skriagin.community.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skriagin.community.dto.category.CategoryResponseDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDto {
    private Long id;
    private CategoryResponseDto category;
    private String description;
    private Double latitude;
    private Double longitude;
    private LocalDateTime dateTime;
}
