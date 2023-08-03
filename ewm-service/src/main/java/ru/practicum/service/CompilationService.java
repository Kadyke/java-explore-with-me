package ru.practicum.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Compilation;

import java.util.List;

import static java.util.stream.Collectors.toSet;

@Service
public class CompilationService {
    private final CompilationRepository repository;
    private final EventService eventService;

    public CompilationService(CompilationRepository repository, EventService eventService) {
        this.repository = repository;
        this.eventService = eventService;
    }


    public Compilation createCompilation(Compilation compilation) {
        compilation.setEvents(compilation.getEvents().stream().map(
                event -> eventService.getEvent(event.getId())).collect(toSet()));
        return repository.save(compilation);
    }

    public void deleteCompilation(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException();
        }
    }

    public Compilation upadteCompilation(Compilation compilation, Long id) {
        Compilation oldCompilation = repository.findById(id).orElseThrow(NotFoundException::new);
        oldCompilation.update(compilation);
        return repository.save(oldCompilation);
    }


    public List<Compilation> getCompilations(Boolean pinned, Integer from, Integer size) {
        return repository.findByPinned(pinned, size, from);
    }

    public Compilation getCompilation(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }
}
