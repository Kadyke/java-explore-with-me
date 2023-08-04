package ru.practicum.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventUpdateDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.State;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    private final EventService service;
    private final StatsClient client;

    public AdminEventController(EventService service, StatsClient client) {
        this.service = service;
        this.client = client;
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<State> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                        @RequestParam(required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd) {
        List<EventFullDto> eventFullDtos = EventMapper.INSTANCE.collectionToEventFullDto(
                service.getEvents(users, states, categories, from, size, Timestamp.valueOf(rangeStart),
                        Timestamp.valueOf(rangeEnd)));
        client.addViewsForEventFullDto(eventFullDtos);
        return eventFullDtos;
    }

    @PatchMapping("/{id}")
    public EventFullDto updateEvent(@PathVariable Long id, @RequestBody @Valid EventUpdateDto eventUpdateDto) {
        EventFullDto eventFullDto = EventMapper.INSTANCE.toEventFullDto(service.updateEventByAdmin(id,
                EventMapper.INSTANCE.toEvent(eventUpdateDto)));
        client.addViewsForEventFullDto(List.of(eventFullDto));
        return eventFullDto;
    }
}
