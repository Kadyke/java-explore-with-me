package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.yandex.Stat;
import ru.practicum.model.Hit;
import ru.practicum.repository.StatsRepository;

import java.sql.Timestamp;
import java.util.List;


@Service
public class StatsService {
    private final StatsRepository repository;

    public StatsService(StatsRepository repository) {
        this.repository = repository;
    }

    public Hit saveHit(Hit hit) {
        return repository.save(hit);
    }

    public List<Stat> getStats(Timestamp end, Timestamp start, Boolean unique) {
        if (unique) {
            return repository.getStatsWithUniqueIp(end, start);
        }
        return repository.getStatsWithAllIp(end, start);
    }

    public List<Stat> getStats(Timestamp end, Timestamp start, List<String> uris, Boolean unique) {
        if (unique) {
            return repository.getStatsWithUniqueIp(end, start, uris);
        }
        return repository.getStatsWithAllIp(end, start, uris);
    }
}
