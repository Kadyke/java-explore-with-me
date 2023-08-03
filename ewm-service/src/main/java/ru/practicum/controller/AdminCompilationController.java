package ru.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.CompilationService;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CompilationInputDto;
import ru.practicum.mapper.CompilationMapper;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final CompilationService service;

    public AdminCompilationController(CompilationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addNewCompilation(@RequestBody @Valid CompilationInputDto compilationInputDto) {
        return CompilationMapper.INSTANCE.toCompilationDto(service.createCompilation(
                CompilationMapper.INSTANCE.toCompilation(compilationInputDto)));
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteCompilation(@PathVariable Long id) {
        service.deleteCompilation(id);
        return HttpStatus.NO_CONTENT;
    }

    @PatchMapping("/{id}")
    public CompilationDto changeCompilation(@PathVariable Long id,
                                            @RequestBody CompilationInputDto compilationInputDto) {
        return CompilationMapper.INSTANCE.toCompilationDto(service.upadteCompilation(
                CompilationMapper.INSTANCE.toCompilation(compilationInputDto), id));
    }
}
