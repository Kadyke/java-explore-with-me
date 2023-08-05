package ru.practicum.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventInputDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.EventUpdateDto;
import ru.practicum.model.Event;
import ru.practicum.model.State;

import java.util.*;


@Mapper(uses = {CategoryMapper.class, UserMapper.class})
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

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
}
