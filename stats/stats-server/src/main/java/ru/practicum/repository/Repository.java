package ru.practicum.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.practicum.yandex.Stat;
import ru.practicum.mapper.HitMapperRep;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Hit;

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
        return jdbcTemplate.queryForObject("SELECT * FROM hits WHERE id = ?", new HitMapperRep(), id);
    }

    public List<Stat> getStatsWithAllIp(Timestamp end, Timestamp start, List<String> uris) {
        StringBuilder builder = new StringBuilder("SELECT app, uri, COUNT(ip) AS cip FROM hits WHERE " +
                "time_date BETWEEN ? AND ? AND uri IN (");
        for (String uri : uris) {
            builder.append("'").append(uri).append("', ");
        }
        String sql = builder.deleteCharAt(builder.length() - 1).deleteCharAt(builder.length() - 1)
                .append(") GROUP BY app, uri ORDER BY cip DESC;").toString();
        return jdbcTemplate.query(sql, new StatMapper(), start, end);
    }

    public List<Stat> getStatsWithAllIp(Timestamp end, Timestamp start) {
        return jdbcTemplate.query("SELECT app, uri, COUNT(ip) AS cip FROM hits WHERE time_date BETWEEN ? AND ? " +
                "GROUP BY app, uri ORDER BY cip DESC;", new StatMapper(), start, end);
    }

    public List<Stat> getStatsWithUniqueIp(Timestamp end, Timestamp start, List<String> uris) {
        StringBuilder builder = new StringBuilder("SELECT app, uri, COUNT(DISTINCT ip) AS cip FROM hits WHERE " +
                "time_date BETWEEN ? AND ? AND uri IN (");
        for (String uri : uris) {
            builder.append("'").append(uri).append("', ");
        }
        String sql = builder.deleteCharAt(builder.length() - 1).deleteCharAt(builder.length() - 1)
                .append(") GROUP BY app, uri ORDER BY cip DESC;").toString();
        return jdbcTemplate.query(sql, new StatMapper(), start, end);
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
