package ru.practicum.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.practicum.model.Stat;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatMapper implements RowMapper<Stat> {
    @Override
    public Stat mapRow(ResultSet rs, int rowNum) throws SQLException {
        Stat stat = new Stat();
        stat.setApp(rs.getString("app"));
        stat.setUri(rs.getString("uri"));
        stat.setHits(rs.getLong("hits"));
        return stat;
    }
}
