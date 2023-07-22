package ru.practicum;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.model.Hit;
import ru.practicum.model.Stat;
import ru.practicum.service.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StatsServerTest {
    @Autowired
    private Service service;
    private final EasyRandom random = new EasyRandom();

    @Test
    void test() {
        Hit hit = random.nextObject(Hit.class);
        hit.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        service.saveHit(hit);
        service.saveHit(hit);
        List<Stat> stats = service.getStats(Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusHours(1)), false);
        assertEquals(1, stats.size());
        assertEquals(hit.getApp(), stats.get(0).getApp());
        assertEquals(hit.getUri(), stats.get(0).getUri());
        assertEquals(2, stats.get(0).getHits());
        stats = service.getStats(Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusHours(1)), true);
        assertEquals(1, stats.get(0).getHits());
        hit = random.nextObject(Hit.class);
        hit.setTimestamp(Timestamp.valueOf(LocalDateTime.now().plusHours(1)));
        service.saveHit(hit);
        stats = service.getStats(Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusHours(1)), true);
        assertEquals(1, stats.size());
        hit.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusMinutes(1)));
        service.saveHit(hit);
        stats = service.getStats(Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusHours(1)), true);
        assertEquals(2, stats.size());
    }
}
