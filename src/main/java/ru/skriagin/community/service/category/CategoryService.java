package ru.skriagin.community.service.category;

import ru.skriagin.community.dto.category.CategoryCreateDto;
import ru.skriagin.community.dto.category.CategoryResponseDto;

public interface CategoryService {
    CategoryResponseDto createCategory(CategoryCreateDto categoryCreateDto);

    CategoryResponseDto getCategory(Long id);
}
