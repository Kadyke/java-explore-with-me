package ru.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Stat;
import ru.practicum.service.Service;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class Controller {
    private final Service service;

    public Controller(Service service) {
        this.service = service;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto saveHit(@RequestBody HitDto hitDto) {
        return HitMapper.toHitDto(service.saveHit(HitMapper.toHit(hitDto)));
    }

    @GetMapping("/stats")
    public List<Stat> getStats() {
        return service.getStats();
    }
}
