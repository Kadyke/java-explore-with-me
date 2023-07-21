package ru.practicum.service;

import ru.practicum.model.Hit;
import ru.practicum.model.Stat;
import ru.practicum.repository.Repository;

import java.util.List;

@org.springframework.stereotype.Service
public class Service {
    private final Repository repository;

    public Service(Repository repository) {
        this.repository = repository;
    }

    public Hit saveHit(Hit hit) {
        return repository.save(hit);
    }

    public List<Stat> getStats() {
        return repository.getStats();
    }
}
