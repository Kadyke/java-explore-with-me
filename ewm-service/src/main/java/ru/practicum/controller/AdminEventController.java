package ru.practicum.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventUpdateDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.State;
import ru.practicum.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    private final EventService service;

    public AdminEventController(EventService service) {
        this.service = service;
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
        if (users.size() == 1 && users.get(0) == 0) {
            users = null;
        }
        if (categories.size() == 1 && categories.get(0) == 0) {
            categories = null;
        }
        return EventMapper.INSTANCE.collectionToEventFullDto(service.getEvents(users, states, categories, from, size,
                rangeStart, rangeEnd));
    }

    @PatchMapping("/{id}")
    public EventFullDto updateEvent(@PathVariable Long id, @RequestBody EventUpdateDto eventUpdateDto) {
        return EventMapper.INSTANCE.toEventFullDto(service.updateEventByAdmin(id,
                EventMapper.INSTANCE.toEvent(eventUpdateDto)));
    }
}
