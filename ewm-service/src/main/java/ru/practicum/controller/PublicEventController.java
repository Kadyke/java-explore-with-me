package ru.practicum.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.*;
import ru.practicum.exception.BadRequestException;
import ru.practicum.model.Location;
import ru.practicum.model.Sort;
import ru.practicum.mapper.EventMapper;
import ru.practicum.service.EventService;
import ru.practicum.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
public class PublicEventController {
    private final EventService eventService;
    private final StatsClient client;

    public PublicEventController(EventService eventService, RequestService requestService, StatsClient client) {
        this.eventService = eventService;
        this.client = client;
    }

    @GetMapping("/{id}")
    public EventFullDto getPublicEvent(@PathVariable Long id, HttpServletRequest request) {
        client.sendHit("/events/" + id, request);
        EventFullDto eventFullDto = EventMapper.INSTANCE.toEventFullDto(eventService.getPublicEvent(id));
        client.addViewsForEventFullDto(List.of(eventFullDto));
        return eventFullDto;
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
        if (rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Дата начала не может быть позже даты конца.");
        }
        client.sendHit("/events", request);
        List<EventShortDto> eventShortDtos =  eventService.getPublicEvents(text, categories, paid, onlyAvailable, sort,
                rangeStart, rangeEnd, from, size);
        client.addViewsForEventShortDto(eventShortDtos);
        return eventShortDtos;
    }

}
