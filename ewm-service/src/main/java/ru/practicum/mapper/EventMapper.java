package ru.practicum.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.ResourceAccessException;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventInputDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.EventUpdateDto;
import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.yandex.Client;

import java.time.LocalDateTime;
import java.util.*;


@Mapper(uses = {CategoryMapper.class, UserMapper.class})
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);
    Logger logger = LoggerFactory.getLogger(EventMapper.class);
    Client client = new Client("http://localhost:9090/");

    @Mapping(source = "category", target = "category.id")
    Event toEvent(EventInputDto eventDto);

    EventFullDto toEventFullDto(Event event);

    EventShortDto toEventShortDto(Event event);

    List<EventShortDto> collectionToEventShortDto(Collection<Event> events);

    List<EventFullDto> collectionToEventFullDto(Collection<Event> events);

    @Mapping(source = "category", target = "category.id")
    Event toEvent(EventUpdateDto eventDto);

    @AfterMapping
    default void mapState(EventUpdateDto eventUpdateDto, @MappingTarget Event event) {
        if (eventUpdateDto.getStateAction() != null) {
            switch (eventUpdateDto.getStateAction()) {
                case CANCEL_REVIEW:
                case REJECT_EVENT:
                    event.setState(State.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case PUBLISH_EVENT:
                    event.setState(State.PUBLISHED);
            }
        }
    }

    @AfterMapping
    default void mapViews(@MappingTarget EventFullDto eventFullDto, Event event) {
        List<String> uris = List.of("/events/" + event.getId());
        try {
            Object responseBody = client.getStats(LocalDateTime.now(),
                    LocalDateTime.of(2000, 1, 1, 0, 0),
                    uris, false).getBody();
            List<Object> stats = (List<Object>) responseBody;
            if (stats.isEmpty()) {
                eventFullDto.setViews(0L);
            } else {
                LinkedHashMap<String, Object> element = (LinkedHashMap<String, Object>) stats.get(0);
                eventFullDto.setViews((Long.valueOf(element.get("hits").toString())));
            }
        } catch (ResourceAccessException e) {
            eventFullDto.setViews(null);
        }
    }

    @AfterMapping
    default void mapViews(@MappingTarget EventShortDto eventShortDto, Event event) {
        List<String> uris = List.of("/events/" + event.getId());
        try {
            Object responseBody = client.getStats(LocalDateTime.now(),
                    LocalDateTime.of(2000, 1, 1, 0, 0),
                    uris, false).getBody();
            List<Object> stats = (List<Object>) responseBody;
            if (stats.isEmpty()) {
                eventShortDto.setViews(0L);
            } else {
                LinkedHashMap<String, Object> element = (LinkedHashMap<String, Object>) stats.get(0);
                eventShortDto.setViews((Long.valueOf(element.get("hits").toString())));
            }
        } catch (ResourceAccessException e) {
            eventShortDto.setViews(null);
        }
    }
}
