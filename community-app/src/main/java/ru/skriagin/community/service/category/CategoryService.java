package ru.skriagin.community.service.category;

import ru.skriagin.community.dto.category.CategoryCreateDto;
import ru.skriagin.community.dto.category.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto createCategory(CategoryCreateDto categoryCreateDto);

    CategoryResponseDto getCategory(Long id);

    List<CategoryResponseDto> getAllCategories();

    void deleteCategory(Long id);
}
