package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.yandex.Client;
import ru.practicum.yandex.HitDto;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
public class StatsClient {
    private final Client client;

    public StatsClient(Client client) {
        this.client = client;
    }

    public void addViewsForEventFullDto(List<EventFullDto> eventFullDtos) {
        for (EventFullDto eventFullDto : eventFullDtos) {
            List<String> uris = List.of("/events/" + eventFullDto.getId());
            try {
                Object responseBody = client.getStats(LocalDateTime.now(),
                        LocalDateTime.of(2000, 1, 1, 0, 0),
                        uris, true).getBody();
                List<Object> stats = (List<Object>) responseBody;
                if (stats.isEmpty()) {
                    eventFullDto.setViews(0L);
                } else {
                    LinkedHashMap<String, Object> element = (LinkedHashMap<String, Object>) stats.get(0);
                    eventFullDto.setViews((Long.valueOf(element.get("hits").toString())));
                }
            } catch (ResourceAccessException e) {
                log.warn("Сервис статистики не доступен.");
                break;
            }
        }
    }

    public void addViewsForEventShortDto(List<EventShortDto> eventShortDtos) {
        for (EventShortDto eventShortDto : eventShortDtos) {
            List<String> uris = List.of("/events/" + eventShortDto.getId());
            try {
                Object responseBody = client.getStats(LocalDateTime.now(),
                        LocalDateTime.of(2000, 1, 1, 0, 0),
                        uris, true).getBody();
                List<Object> stats = (List<Object>) responseBody;
                if (stats.isEmpty()) {
                    eventShortDto.setViews(0L);
                } else {
                    LinkedHashMap<String, Object> element = (LinkedHashMap<String, Object>) stats.get(0);
                    eventShortDto.setViews((Long.valueOf(element.get("hits").toString())));
                }
            } catch (ResourceAccessException e) {
                log.warn("Сервис статистики не доступен.");
                break;
            }
        }
    }

    public void sendHit(String uri, HttpServletRequest request) {
        try {
        client.saveHit(new HitDto("ewm-service", uri, request.getRemoteAddr(),
                Timestamp.valueOf(LocalDateTime.now())));
        } catch (ResourceAccessException e) {
            log.warn("Сервис статистики не доступен.");
        }
    }
}
