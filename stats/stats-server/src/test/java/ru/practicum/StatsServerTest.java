package ru.practicum;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.model.Hit;
import ru.practicum.service.StatsService;
import ru.practicum.yandex.Stat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StatsServerTest {
    @Autowired
    private StatsService statsService;
    private final EasyRandom random = new EasyRandom();

    @Test
    @DirtiesContext
    void test() {
        Hit hit = random.nextObject(Hit.class);
        hit.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        statsService.saveHit(hit);
        statsService.saveHit(hit);
        List<Stat> stats = statsService.getStats(Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusHours(1)), false);
        assertEquals(1, stats.size());
        assertEquals(hit.getApp(), stats.get(0).getApp());
        assertEquals(hit.getUri(), stats.get(0).getUri());
        assertEquals(2, stats.get(0).getHits());
        stats = statsService.getStats(Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusHours(1)), true);
        assertEquals(1, stats.get(0).getHits());
        hit = random.nextObject(Hit.class);
        hit.setTimestamp(Timestamp.valueOf(LocalDateTime.now().plusHours(1)));
        statsService.saveHit(hit);
        stats = statsService.getStats(Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusHours(1)), true);
        assertEquals(1, stats.size());
        hit.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusMinutes(1)));
        statsService.saveHit(hit);
        stats = statsService.getStats(Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusHours(1)), true);
        assertEquals(2, stats.size());
    }

    @Test
    @DirtiesContext
    void testSort() {
        Hit hit = random.nextObject(Hit.class);
        hit.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        statsService.saveHit(hit);
        statsService.saveHit(hit);
        Hit otherHit = random.nextObject(Hit.class);
        otherHit.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        statsService.saveHit(otherHit);
        List<String> uris = new ArrayList<>();
        uris.add(hit.getUri());
        uris.add(otherHit.getUri());
        List<Stat> stats = statsService.getStats(Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().minusHours(1)), uris, false);
        assertEquals(2, stats.size());
        assertEquals(hit.getApp(), stats.get(0).getApp());
        assertEquals(2, stats.get(0).getHits());
        assertEquals(otherHit.getApp(), stats.get(1).getApp());
        assertEquals(1, stats.get(1).getHits());
    }
}
