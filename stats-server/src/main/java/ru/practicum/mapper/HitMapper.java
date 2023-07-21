package ru.practicum.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.practicum.dto.HitDto;
import ru.practicum.model.Hit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HitMapper implements RowMapper<Hit> {

    public static Hit toHit(HitDto hitDto) {
        Hit hit =  new Hit();
        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setTimestamp(hitDto.getTimestamp());
        return hit;
    }

    public static HitDto toHitDto(Hit hit) {
        HitDto hitDto =  new HitDto();
        hitDto.setApp(hit.getApp());
        hitDto.setUri(hit.getUri());
        hitDto.setIp(hit.getIp());
        hitDto.setTimestamp(hit.getTimestamp());
        return hitDto;
    }

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
