package ru.skriagin.community.controller.publ;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skriagin.community.dto.category.CategoryCreateDto;
import ru.skriagin.community.dto.category.CategoryResponseDto;
import ru.skriagin.community.service.category.CategoryService;

@RequestMapping("/category")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@RequestBody @Valid CategoryCreateDto categoryCreateDto) {
        log.info("CONTROLLER: Получен запрос на создание категории с названием {}", categoryCreateDto.getName());
        return categoryService.createCategory(categoryCreateDto);
    }

    @GetMapping("/{categoryId}")
    public CategoryResponseDto getCategory(@PathVariable @Min(1) Long categoryId) {
        log.info("CONTROLLER: Получен запрос на получение категории с id {}", categoryId);
        return categoryService.getCategory(categoryId);
    }

}
