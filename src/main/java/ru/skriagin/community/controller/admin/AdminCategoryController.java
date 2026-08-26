package ru.skriagin.community.controller.admin;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skriagin.community.service.category.CategoryService;

@RestController
@RequestMapping("/admin/panel/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {

    private final CategoryService categoryService;

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Min(1) Long categoryId) {
        log.info("CONTROLLER: получен запрос на удаление категории {}", categoryId);
        categoryService.deleteCategory(categoryId);
    }
}
