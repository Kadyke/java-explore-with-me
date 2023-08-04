package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.dto.EventShortDto;
import ru.practicum.exception.EventIsNotPendingException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Sort;
import ru.practicum.exception.EventAlreadyPublishedException;
import ru.practicum.exception.EventDateException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class EventService {
    private final EventRepository repository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventService(EventRepository repository, UserService userService, CategoryService categoryService) {
        this.repository = repository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public Event createEvent(Event event, Long userId) {
        event.setInitiator(userService.getUser(userId));
        event.setCreatedOn(LocalDateTime.now());
        event.setCategory(categoryService.getCategory(event.getCategory().getId()));
        event.setConfirmedRequests(0);
        event.setState(State.PENDING);
        return repository.save(event);
    }

    public List<Event> getUsersEvents(Long userId, Integer from, Integer size) {
        userService.getUser(userId);
        return repository.findByUserId(userId, size, from);
    }

    public Event getUsersEvent(Long userId, Long id) {
        User user = userService.getUser(userId);
        return repository.findByIdAndInitiator(id, user).orElseThrow(NotFoundException::new);
    }

    public Event updateEventByUser(Long userId, Long id, Event event) {
        User user = userService.getUser(userId);
        Event oldEvent = repository.findById(id).orElseThrow(NotFoundException::new);
        if (!user.equals(oldEvent.getInitiator())) {
            throw new NotFoundException();
        }
        if (oldEvent.getState() == State.PUBLISHED) {
            throw new EventAlreadyPublishedException();
        }
        if (event.getCategory().getId() != null) {
            event.setCategory(categoryService.getCategory(event.getCategory().getId()));
        } else {
            event.setCategory(null);
        }
        oldEvent.update(event);
        return repository.save(oldEvent);
    }

    public List<Event> getEvents(List<Long> users, List<State> states, List<Long> categories, Integer from,
                                 Integer size, Timestamp rangeStart, Timestamp rangeEnd) {
        List<String> statesInString = new ArrayList<>();
        if (states != null) {
            for (State state: states) {
                statesInString.add(state.toString());
            }
        }
        return repository.getEvents(users, size, categories, from, statesInString, rangeStart, rangeEnd);

    }

    public Event updateEventByAdmin(Long id, Event event) {
        Event oldEvent = repository.findById(id).orElseThrow(NotFoundException::new);
        if (oldEvent.getState() != State.PENDING) {
            throw new EventIsNotPendingException();
        }
        if (event.getCategory().getId() != null) {
            event.setCategory(categoryService.getCategory(event.getCategory().getId()));
        } else {
            event.setCategory(null);
        }
        if (event.getState() == State.PUBLISHED) {
            LocalDateTime now = LocalDateTime.now();
            oldEvent.update(event);
            if (oldEvent.getEventDate().isBefore(now) &&
                    Duration.between(oldEvent.getEventDate(), now).toHours() > 1) {
                throw new EventDateException("Публикация не может быть позже более чем на час даты начала события.");
            }
            oldEvent.setPublishedOn(now);
            return repository.save(oldEvent);
        }
        oldEvent.update(event);
        return repository.save(oldEvent);
    }

    Event getEvent(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    void increaseConfirmedRequest(Long id) {
        repository.increaseConfirmedRequest(id);
    }

    void decreaseConfirmedRequest(Long id) {
        repository.decreaseConfirmedRequest(id);
    }

    void saveEvent(Event event) {
        repository.save(event);
    }

    public List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid, Boolean onlyAvailable,
                                               Sort sort, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                               Integer size) {
        if (text != null) {
            text = "%" + text.toLowerCase() + "%";
        }
        List<Event> events;
        if (onlyAvailable) {
            events = repository.getPublicEventsOnlyAvailableSortByDate(text, paid, categories,
                    Timestamp.valueOf(rangeStart), Timestamp.valueOf(rangeEnd),
                    size, from);
        } else {
            events = repository.getPublicEventsSortByDate(text, paid, categories, Timestamp.valueOf(rangeStart),
                    Timestamp.valueOf(rangeEnd), size, from);
        }
        if (sort.equals(Sort.VIEWS)) {
            return EventMapper.INSTANCE.collectionToEventShortDto(events).stream().sorted(
                    (o1, o2) -> {
                        if (o1.getViews() == null) {
                            return 0;
                        }
                        return Integer.parseInt(Long.toString(o1.getViews() - o2.getViews()));
                    }).collect(toList());
        }
        return EventMapper.INSTANCE.collectionToEventShortDto(events);
    }

    public Event getPublicEvent(Long id) {
        return repository.findByIdAndState(id, State.PUBLISHED).orElseThrow(NotFoundException::new);
    }
}
