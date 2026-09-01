package ru.skriagin.community.controller.publ;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.skriagin.community.dto.category.CategoryResponseDto;
import ru.skriagin.community.service.category.CategoryService;

@RequestMapping("/category")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    public CategoryResponseDto getCategory(@PathVariable @Min(1) Long categoryId) {
        log.info("CONTROLLER: Получен запрос на получение категории с id {}", categoryId);
        return categoryService.getCategory(categoryId);
    }

}
