package ru.skriagin.community.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.skriagin.community.dto.category.CategoryCreateDto;
import ru.skriagin.community.dto.category.CategoryResponseDto;
import ru.skriagin.community.model.Category;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category toEntity(CategoryCreateDto categoryCreateDto);
    CategoryResponseDto toResponseDto(Category category);
}
