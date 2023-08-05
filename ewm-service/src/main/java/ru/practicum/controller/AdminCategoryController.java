package ru.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    private final CategoryService service;

    public AdminCategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addNewCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return CategoryMapper.INSTANCE.toCategoryDto(service.createCategory(
                CategoryMapper.INSTANCE.toCategory(categoryDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public HttpStatus deleteCategory(@PathVariable Long id) {
        service.deleteCategory(id);
        return HttpStatus.NO_CONTENT;
    }

    @PatchMapping("/{id}")
    public CategoryDto changeCategory(@PathVariable Long id, @RequestBody @Valid CategoryDto categoryDto) {
        categoryDto.setId(id);
        return CategoryMapper.INSTANCE.toCategoryDto(service.changeCategory(
                CategoryMapper.INSTANCE.toCategory(categoryDto)));
    }
}
