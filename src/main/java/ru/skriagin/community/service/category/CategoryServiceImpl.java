package ru.skriagin.community.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skriagin.community.dto.category.CategoryCreateDto;
import ru.skriagin.community.dto.category.CategoryResponseDto;
import ru.skriagin.community.exception.EntityNotFoundException;
import ru.skriagin.community.mapper.CategoryMapper;
import ru.skriagin.community.model.Category;
import ru.skriagin.community.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Override
    public CategoryResponseDto createCategory(CategoryCreateDto categoryCreateDto) {
        Category createCategory = categoryMapper.toEntity(categoryCreateDto);
        return categoryMapper.toResponseDto(categoryRepository.save(createCategory));
    }

    @Override
    public CategoryResponseDto getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category", id));
        return categoryMapper.toResponseDto(category);
    }
}
