package ru.practicum.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.practicum.mapper.HitMapper;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Hit;
import ru.practicum.model.Stat;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Component
public class Repository {
    private final JdbcTemplate jdbcTemplate;

    public Repository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Hit save(Hit hit) {
        Long id = getId(hit.getApp());
        jdbcTemplate.update("UPDATE hits SET uri = ?, ip = ?, time_date = ? WHERE id = ?;", hit.getUri(),
                hit.getIp(), hit.getTimestamp(), id);
        return jdbcTemplate.queryForObject("SELECT * FROM hits WHERE id = ?", new HitMapper(), id);
    }

    public List<Stat> getStatsWithAllIp(Timestamp end, Timestamp start, String uri) {
        return jdbcTemplate.query("SELECT app, uri, COUNT(ip) AS cip FROM hits WHERE time_date BETWEEN ? AND ? " +
                        "GROUP BY app, uri HAVING uri = ? ORDER BY cip DESC;", new StatMapper(), start, end, uri);
    }

    public List<Stat> getStatsWithAllIp(Timestamp end, Timestamp start) {
        return jdbcTemplate.query("SELECT app, uri, COUNT(ip) AS cip FROM hits WHERE time_date BETWEEN ? AND ? " +
                "GROUP BY app, uri ORDER BY cip DESC;", new StatMapper(), start, end);
    }

    public List<Stat> getStatsWithUniqueIp(Timestamp end, Timestamp start, String uri) {
        return jdbcTemplate.query("SELECT app, uri, COUNT(DISTINCT ip) AS cip FROM hits WHERE time_date BETWEEN ? " +
                        "AND ? GROUP BY app, uri HAVING uri = ? ORDER BY cip DESC;", new StatMapper(), start, end, uri);
    }

    public List<Stat> getStatsWithUniqueIp(Timestamp end, Timestamp start) {
        return jdbcTemplate.query("SELECT app, uri, COUNT(DISTINCT ip) AS cip FROM hits WHERE time_date BETWEEN ? " +
                        "AND ? GROUP BY app, uri ORDER BY cip DESC;", new StatMapper(), start, end);
    }

    private Long getId(String message) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO hits (app) VALUES(?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, message);
            return ps;
        }, keyHolder);
        return (Long) keyHolder.getKeys().get("id");
    }
}
