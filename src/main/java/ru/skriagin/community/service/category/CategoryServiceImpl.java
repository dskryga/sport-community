package ru.skriagin.community.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.skriagin.community.dto.category.CategoryCreateDto;
import ru.skriagin.community.dto.category.CategoryResponseDto;
import ru.skriagin.community.exception.EntityNotFoundException;
import ru.skriagin.community.mapper.CategoryMapper;
import ru.skriagin.community.model.Category;
import ru.skriagin.community.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CacheManager cacheManager;

    @Override
    public CategoryResponseDto createCategory(CategoryCreateDto categoryCreateDto) {
        Category createCategory = categoryMapper.toEntity(categoryCreateDto);
        createCategory = categoryRepository.save(createCategory);
        log.info("Категория с id {} сохранена", createCategory.getId());

        clearCategoriesCache();

        return categoryMapper.toResponseDto(createCategory);
    }

    @Override
    @Cacheable(value = "categories", key = "#id")
    public CategoryResponseDto getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Категория с id {} не найдена", id);
                    return new EntityNotFoundException("Category", id);
                });
        return categoryMapper.toResponseDto(category);
    }

    @Override
    @Cacheable(value = "categories", key = "'all'")
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category", id);
        }
        categoryRepository.deleteById(id);
        log.info("Категория с id {} удалена", id);

        clearCategoriesCache();
        clearCache("events");
    }

    private void clearCategoriesCache() {
        clearCache("categories");
    }

    private void clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }
}
