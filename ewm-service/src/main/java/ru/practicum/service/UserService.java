package ru.practicum.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.exception.EmailAlreadyExistException;
import ru.practicum.exception.NotFoundException;

import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(User user) {
        try {
            return repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistException();
        }
    }

    public void deleteUser(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException();
        }
    }

    public Collection<User> getUsers(Integer from, Integer size) {
        return repository.getUserByLimit(size, from);
    }

    public Collection<User> getUsers(Integer from, Integer size, List<Long> ids) {
        return repository.getUserByLimit(ids, size, from);
    }

    User getUser(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }
}
