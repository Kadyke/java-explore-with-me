package ru.practicum.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.practicum.model.Hit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HitMapperRep implements RowMapper<Hit> {

    @Override
    public Hit mapRow(ResultSet rs, int rowNum) throws SQLException {
        Hit hit = new Hit();
        hit.setId(rs.getLong("id"));
        hit.setApp(rs.getString("app"));
        hit.setUri(rs.getString("uri"));
        hit.setIp(rs.getString("ip"));
        hit.setTimestamp(rs.getTimestamp("time_date"));
        return hit;
    }
}
