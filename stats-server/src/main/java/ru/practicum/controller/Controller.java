package ru.practicum.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Stat;
import ru.practicum.service.StatsService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class Controller {
    private final StatsService statsService;

    public Controller(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto saveHit(@RequestBody HitDto hitDto) {
        return HitMapper.toHitDto(statsService.saveHit(HitMapper.toHit(hitDto)));
    }

    @GetMapping("/stats")
    public List<Stat> getStats(@RequestParam(name = "end") @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                               @RequestParam(name = "start") @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                               @RequestParam(required = false) List<String> uris,
                               @RequestParam(defaultValue = "false") Boolean unique) {
        if (uris == null) {
            return statsService.getStats(Timestamp.valueOf(end), Timestamp.valueOf(start), unique);
        }
        return statsService.getStats(Timestamp.valueOf(end), Timestamp.valueOf(start), uris, unique);
    }
}
