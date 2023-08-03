package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.*;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Mapper(uses = {EventMapper.class})
public interface CompilationMapper {
    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    Compilation toCompilation(CompilationInputDto compilationInputDto);

    default Set<Event> toCompilation(Set<Long> eventIds) {
        if (eventIds == null) {
            return null;
        }
        Set<Event> events = new HashSet<>();
        for (Long id : eventIds) {
            Event event = new Event();
            event.setId(id);
            events.add(event);
        }
        return events;
    }

    CompilationDto toCompilationDto(Compilation compilation);

    List<CompilationDto> collectionToCompilationDto(Collection<Compilation> compilation);
}
