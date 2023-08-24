package ru.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.*;
import ru.practicum.model.*;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LikeRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final LikeRepository likeRepository;

    public EventService(EventRepository eventRepository, UserService userService, CategoryService categoryService, LikeRepository likeRepository) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.likeRepository = likeRepository;
    }

    @Transactional
    public Event createEvent(Event event, Long userId) {
        event.setInitiator(userService.getUser(userId));
        event.setCreatedOn(LocalDateTime.now());
        event.setCategory(categoryService.getCategory(event.getCategory().getId()));
        event.setConfirmedRequests(0);
        event.setState(State.PENDING);
        return eventRepository.save(event);
    }

    public List<Event> getUsersEvents(Long userId, Integer from, Integer size) {
        userService.getUser(userId);
        List<Event> events = eventRepository.findByUserId(userId, size, from);
        for (Event event : events) {
            if (event.getState() == State.PUBLISHED) {
                setLikes(event);
            }
        }
        return events;
    }

    public Event getUsersEvent(Long userId, Long id) {
        User user = userService.getUser(userId);
        Event event = eventRepository.findByIdAndInitiator(id, user).orElseThrow(NotFoundException::new);
        if (event.getState() == State.PUBLISHED) {
            setLikes(event);
        }
        return event;
    }

    @Transactional
    public Event updateEventByUser(Long userId, Long id, Event event) {
        User user = userService.getUser(userId);
        Event oldEvent = eventRepository.findById(id).orElseThrow(NotFoundException::new);
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
        return eventRepository.save(oldEvent);
    }

    public List<Event> getEvents(List<Long> users, List<State> states, List<Long> categories, Integer from,
                                 Integer size, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        List<String> statesInString = new ArrayList<>();
        if (states != null) {
            for (State state: states) {
                statesInString.add(state.toString());
            }
        }
        return eventRepository.getEvents(users, size, categories, from, statesInString, rangeStart, rangeEnd);

    }

    @Transactional
    public Event updateEventByAdmin(Long id, Event event) {
        Event oldEvent = eventRepository.findById(id).orElseThrow(NotFoundException::new);
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
            return eventRepository.save(oldEvent);
        }
        oldEvent.update(event);
        return eventRepository.save(oldEvent);
    }

    Event getEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    void increaseConfirmedRequest(Long id) {
        eventRepository.increaseConfirmedRequest(id);
    }

    @Transactional
    void decreaseConfirmedRequest(Long id) {
        eventRepository.decreaseConfirmedRequest(id);
    }

    @Transactional
    void saveEvent(Event event) {
        eventRepository.save(event);
    }

    public List<Event> getPublicEvents(String text, List<Long> categories, Boolean paid, Boolean onlyAvailable,
                                               Sort sort, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                               Integer size) {
        if (text != null) {
            text = "%" + text.toLowerCase() + "%";
        }
        List<Event> events;
        if (onlyAvailable) {
            events = eventRepository.getPublicEventsOnlyAvailableSortByDate(text, paid, categories,
                    rangeStart, rangeEnd,
                    size, from);
        } else {
            events = eventRepository.getPublicEventsSortByDate(text, paid, categories, rangeStart,
                    rangeEnd, size, from);
        }
        for (Event event: events) {
            setLikes(event);
        }
        return events;
    }

    public Event getPublicEvent(Long id) {
        return setLikes(eventRepository.findByIdAndState(id, State.PUBLISHED).orElseThrow(NotFoundException::new));
    }

    @Transactional
    public Event addLike(Long userId, Long id, Boolean isLiked) {
        User user = userService.getUser(userId);
        Event event = eventRepository.findById(id).orElseThrow(NotFoundException::new);
        if (event.getState() != State.PUBLISHED) {
            throw new EventIsNotPublishedException();
        }
        Like like = new Like(new LikeId(userId, id), isLiked);
        likeRepository.save(like);
        return setLikes(event);
    }

    private Event setLikes(Event event) {
        event.setLikes(likeRepository.findByEventIdAndIsLiked(event.getId(), true).size());
        event.setDislikes(likeRepository.findByEventIdAndIsLiked(event.getId(), false).size());
        return event;
    }
}
