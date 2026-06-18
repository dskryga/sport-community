package ru.skriagin.community.controller.publ;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skriagin.community.dto.category.CategoryCreateDto;
import ru.skriagin.community.dto.category.CategoryResponseDto;
import ru.skriagin.community.service.category.CategoryService;

@RequestMapping("/category")
@RestController
@RequiredArgsConstructor
public class PublicCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@RequestBody @Valid CategoryCreateDto categoryCreateDto) {
        return categoryService.createCategory(categoryCreateDto);
    }

    @GetMapping("/{categoryId}")
    public CategoryResponseDto getCategory(@PathVariable @Min(1) Long categoryId) {
        return categoryService.getCategory(categoryId);
    }

}
