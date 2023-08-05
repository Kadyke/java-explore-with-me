package ru.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationUpdateDto;
import ru.practicum.service.CompilationService;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CompilationNewDto;
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
    public CompilationDto addNewCompilation(@RequestBody @Valid CompilationNewDto compilationNewDto) {
        return CompilationMapper.INSTANCE.toCompilationDto(service.createCompilation(
                CompilationMapper.INSTANCE.toCompilation(compilationNewDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public HttpStatus deleteCompilation(@PathVariable Long id) {
        service.deleteCompilation(id);
        return HttpStatus.NO_CONTENT;
    }

    @PatchMapping("/{id}")
    public CompilationDto changeCompilation(@PathVariable Long id, @Valid
                                            @RequestBody CompilationUpdateDto compilationUpdateDto) {
        return CompilationMapper.INSTANCE.toCompilationDto(service.updateCompilation(
                CompilationMapper.INSTANCE.toCompilation(compilationUpdateDto), id));
    }
}
