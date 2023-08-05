package ru.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.*;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.service.EventService;
import ru.practicum.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventController {
    private final EventService eventService;
    private final RequestService requestService;
    private final StatsClient client;

    public PrivateEventController(EventService eventService, RequestService requestService, StatsClient client) {
        this.eventService = eventService;
        this.requestService = requestService;
        this.client = client;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEvent(@PathVariable Long userId, @RequestBody @Valid EventInputDto eventDto) {
        EventFullDto eventFullDto = EventMapper.INSTANCE
                        .toEventFullDto(eventService.createEvent(EventMapper.INSTANCE.toEvent(eventDto), userId));
        client.addViewsForEventFullDto(List.of(eventFullDto));
        return eventFullDto;
    }

    @GetMapping("/{id}")
    public EventFullDto getUsersEvent(@PathVariable Long userId, @PathVariable Long id) {
        EventFullDto eventFullDto = EventMapper.INSTANCE.toEventFullDto(eventService.getUsersEvent(userId, id));
        client.addViewsForEventFullDto(List.of(eventFullDto));
        return eventFullDto;
    }

    @GetMapping
    public List<EventShortDto> getUsersEvents(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        List<EventShortDto> eventShortDtos = EventMapper.INSTANCE.collectionToEventShortDto(
                eventService.getUsersEvents(userId, from, size));
        client.addViewsForEventShortDto(eventShortDtos);
        return eventShortDtos;
    }

    @PatchMapping("/{id}")
    public EventFullDto updateEvent(@PathVariable Long userId, @PathVariable Long id,
                             @RequestBody @Valid EventUpdateDto eventUpdateDto) {
        EventFullDto eventFullDto =  EventMapper.INSTANCE.toEventFullDto(eventService.updateEventByUser(userId, id,
                EventMapper.INSTANCE.toEvent(eventUpdateDto)));
        client.addViewsForEventFullDto(List.of(eventFullDto));
        return eventFullDto;
    }

    @GetMapping("/{id}/requests")
    public List<RequestDto> getRequestsToEvent(@PathVariable Long userId, @PathVariable Long id) {
        return RequestMapper.INSTANCE.collectionToRequestDto(requestService.getRequestsToEvent(userId, id));
    }

    @PatchMapping("/{id}/requests")
    public ResponseToRequestResult responseToRequest(@PathVariable Long userId, @PathVariable Long id,
                                               @RequestBody @Valid ResponseToRequest responseToRequest) {
        return requestService.responseToRequest(userId, id, responseToRequest);
    }
}
