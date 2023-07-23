package ru.practicum.service;

import ru.practicum.model.Hit;
import ru.practicum.model.Stat;
import ru.practicum.repository.Repository;

import java.sql.Timestamp;
import java.util.List;


@org.springframework.stereotype.Service
public class StatsService {
    private final Repository repository;

    public StatsService(Repository repository) {
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
