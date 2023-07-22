package ru.practicum.service;

import ru.practicum.model.Hit;
import ru.practicum.model.Stat;
import ru.practicum.repository.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
        List<Stat> stats = new ArrayList<>();
        if (unique) {
            for (String uri : uris) {
                stats.addAll(repository.getStatsWithUniqueIp(end, start, uri));
            }
            return stats.stream().sorted((o1, o2) -> (int) (o2.getHits() - o1.getHits())).collect(toList());
        }
        for (String uri : uris) {
            stats.addAll(repository.getStatsWithAllIp(end, start, uri));
        }
        return stats.stream().sorted((o1, o2) -> (int) (o2.getHits() - o1.getHits())).collect(toList());
    }
}
