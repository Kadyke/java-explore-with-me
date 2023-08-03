package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.*;
import ru.practicum.model.Request;

import java.util.Collection;
import java.util.List;


@Mapper(uses = {EventMapper.class, UserMapper.class})
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(source = "event", target = "event.id")
    @Mapping(source = "requester", target = "requester.id")
    Request toRequest(RequestDto requestDto);
    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    RequestDto toRequestDto(Request request);

    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    List<RequestDto> collectionToRequestDto(Collection<Request> requests);
}
