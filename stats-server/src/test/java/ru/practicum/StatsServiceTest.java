package ru.practicum;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.model.Hit;
import ru.practicum.model.Stat;
import ru.practicum.service.Service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StatsServiceTest {
    @Autowired
    private Service service;
    private final EasyRandom random = new EasyRandom();

    @Test
    void test() {
        Hit hit = random.nextObject(Hit.class);
        service.saveHit(hit);
        service.saveHit(hit);
        List<Stat> stats = service.getStats();
        assertEquals(1, stats.size());
        assertEquals(hit.getApp(), stats.get(0).getApp());
        assertEquals(hit.getUri(), stats.get(0).getUri());
        assertEquals(2, stats.get(0).getHits());
    }
}
