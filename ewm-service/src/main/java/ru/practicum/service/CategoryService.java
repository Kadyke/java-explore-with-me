package ru.practicum.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.exception.CategoryWithEventsException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.exception.CategoryAlreadyExistException;
import ru.practicum.exception.NotFoundException;


import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category createCategory(Category category) {
        try {
            return repository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new CategoryAlreadyExistException();
        }
    }

    public void deleteCategory(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException();
        } catch (DataIntegrityViolationException e) {
            throw new CategoryWithEventsException();
        }
    }

    public Category changeCategory(Category category) {
        repository.findById(category.getId()).orElseThrow(NotFoundException::new);
        try {
            return repository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new CategoryAlreadyExistException();
        }
    }

    public Category getCategory(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Category> getCategories(Integer from, Integer size) {
        return repository.findByLimitAndOffset(size, from);
    }
}

