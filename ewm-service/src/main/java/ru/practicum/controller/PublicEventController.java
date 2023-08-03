package ru.practicum.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.model.Sort;
import ru.practicum.mapper.EventMapper;
import ru.practicum.service.EventService;
import ru.practicum.service.RequestService;
import ru.practicum.yandex.Client;
import ru.practicum.yandex.HitDto;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
public class PublicEventController {
    private final EventService eventService;
    private final Client client;

    public PublicEventController(EventService eventService, RequestService requestService) {
        this.eventService = eventService;
        this.client = new Client("http://localhost:9090/");
    }

    @GetMapping("/{id}")
    public EventFullDto getPublicEvent(@PathVariable Long id, HttpServletRequest request) {
        client.saveHit(new HitDto("ewm-service", "/events" + id, request.getRemoteAddr(),
                Timestamp.valueOf(LocalDateTime.now())));
        return EventMapper.INSTANCE.toEventFullDto(eventService.getPublicEvent(id));
    }
    @GetMapping
    public List<EventShortDto> getPublicEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                               @RequestParam(required = false)
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(defaultValue = "EVENT_DATE") Sort sort, HttpServletRequest request) {
        LocalDateTime now = LocalDateTime.now();
        if (rangeStart == null) {
            rangeStart = now;
        }
        if (rangeEnd == null) {
            rangeEnd = now.plusYears(10);
        }
        client.saveHit(new HitDto("ewm-service", "/events", request.getRemoteAddr(), Timestamp.valueOf(now)));
        return eventService.getPublicEvents(text, categories, paid, onlyAvailable, sort, rangeStart, rangeEnd, from, size);
    }

}
