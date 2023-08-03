package ru.practicum.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.yandex.HitDto;
import ru.practicum.yandex.Stat;
import ru.practicum.mapper.HitMapper;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpStatus saveHit(@RequestBody @Valid HitDto hitDto) {
        statsService.saveHit(HitMapper.toHit(hitDto));
        return HttpStatus.CREATED;
    }

    @GetMapping("/stats")
    public List<Stat> getStats(@RequestParam(name = "end")
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                               @RequestParam(name = "start")
                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                               @RequestParam(required = false) List<String> uris,
                               @RequestParam(defaultValue = "false") Boolean unique) {
        if (uris == null) {
            return statsService.getStats(Timestamp.valueOf(end), Timestamp.valueOf(start), unique);
        }
        return statsService.getStats(Timestamp.valueOf(end), Timestamp.valueOf(start), uris, unique);
    }
}
