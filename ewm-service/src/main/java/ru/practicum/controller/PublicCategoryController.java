package ru.practicum.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
public class PublicCategoryController {
    private final CategoryService service;

    public PublicCategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return CategoryMapper.INSTANCE.collectionToCategoryDto(service.getCategories(from, size));
    }

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable Long id) {
        return CategoryMapper.INSTANCE.toCategoryDto(service.getCategory(id));
    }
}
