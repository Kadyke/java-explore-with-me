package ru.practicum.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
public class PublicCompilationController {
    private final CompilationService service;

    public PublicCompilationController(CompilationService service) {
        this.service = service;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        return CompilationMapper.INSTANCE.collectionToCompilationDto(service.getCompilations(pinned, from, size));
    }

    @GetMapping("/{id}")
    public CompilationDto getCompilation(@PathVariable Long id) {
        return CompilationMapper.INSTANCE.toCompilationDto(service.getCompilation(id));
    }
}
