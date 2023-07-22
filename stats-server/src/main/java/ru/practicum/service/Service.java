package ru.practicum.service;

import ru.practicum.model.Hit;
import ru.practicum.model.Stat;
import ru.practicum.repository.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    public List<Stat> getStats(Timestamp end, Timestamp start, Boolean unique) {
        if (unique) {
            return repository.getStatsWithUniqueIp(end, start);
        }
        return repository.getStatsWithAllIp(end, start);
    }

    public List<Stat> getStats(Timestamp end, Timestamp start, List<String> uris, Boolean unique) {
        List<Stat> stats = new ArrayList<>();
        if (unique) {
            for (String uri : uris) {
                stats.addAll(repository.getStatsWithUniqueIp(end, start, uri));
            }
            return stats;
        }
        for (String uri : uris) {
            stats.addAll(repository.getStatsWithAllIp(end, start, uri));
        }
        return stats;
    }
}
