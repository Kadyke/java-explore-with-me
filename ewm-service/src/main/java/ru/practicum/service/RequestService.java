package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.dto.ResponseToRequest;
import ru.practicum.dto.ResponseToRequestResult;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.*;
import ru.practicum.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestService {
    private final RequestRepository repository;
    private final UserService userService;
    private final EventService eventService;

    public RequestService(RequestRepository repository, UserService userService, EventService eventService) {
        this.repository = repository;
        this.userService = userService;
        this.eventService = eventService;
    }

    public Request createRequest(Long userId, Long eventId) {
        User user = userService.getUser(userId);
        Event event = eventService.getEvent(eventId);
        if (event.getInitiator().equals(user)) {
            throw new RequestException("Инициатор события не может отправлять запрос на участия в нем.");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new RequestException("Нельзя участвовать в неопубликованном событии.");
        }
        if (event.getConfirmedRequests().equals(event.getParticipantLimit()) && event.getParticipantLimit() != 0) {
            throw new RequestException("Нет свободных мест для участия.");
        }
        if (repository.findByRequesterAndEvent(user, event).isPresent()) {
            throw new RequestException("Запрос уже был отправлен.");
        }
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setRequester(user);
        request.setEvent(event);
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(Status.PENDING);
        } else {
            request.setStatus(Status.CONFIRMED);
            eventService.increaseConfirmedRequest(eventId);
        }
        return repository.save(request);
    }

    public List<Request> getUsersRequests(Long userId) {
        User user = userService.getUser(userId);
        return repository.findAllByRequester(user);
    }

    public Request cancelRequest(Long userId, Long id) {
        User user = userService.getUser(userId);
        Request request = repository.findById(id).orElseThrow(NotFoundException::new);
        if (!user.equals(request.getRequester())) {
            throw new RequestException("Можно отменять только собственные заявки.");
        }
        if (request.getStatus().equals(Status.CONFIRMED)) {
            eventService.decreaseConfirmedRequest(request.getEvent().getId());
        }
        request.setStatus(Status.CANCELED);
        return repository.save(request);
    }

    public List<Request> getRequestsToEvent(Long userId, Long eventId) {
        User user = userService.getUser(userId);
        Event event = eventService.getEvent(eventId);
        if (!event.getInitiator().equals(user)) {
            throw new RequestException("Просматривать запросы может только инициатор события.");
        }
        return repository.findByEvent(event);
    }

    public ResponseToRequestResult responseToRequest(Long userId, Long eventId, ResponseToRequest responseToRequest) {
        User user = userService.getUser(userId);
        Event event = eventService.getEvent(eventId);
        if (!event.getInitiator().equals(user)) {
            throw new RequestException("Отвечать на запросы может только инициатор события.");
        }
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();
        if (responseToRequest.getStatus().equals(Status.CONFIRMED) && event.getParticipantLimit() != 0) {
            if (event.getConfirmedRequests().equals(event.getParticipantLimit()) ) {
                throw new RequestException("Нет свободных мест для участия.");
            }
            Boolean isFull = false;
            for (Long id: responseToRequest.getRequestIds()) {
                if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
                    isFull = true;
                }
                Request request = repository.findById(id).orElseThrow(NotFoundException::new);
                if (request.getStatus() != Status.PENDING) {
                    throw new RequestException("На заявку уже был отправлен ответ.");
                }
                if (isFull) {
                    request.setStatus(Status.REJECTED);
                    rejectedRequests.add(request);
                } else {
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    request.setStatus(Status.CONFIRMED);
                    confirmedRequests.add(request);
                }
            }
            if (isFull) {
                repository.rejectLastRequest();
            }
        }
        if (responseToRequest.getStatus().equals(Status.CONFIRMED) && event.getParticipantLimit() == 0) {
            for (Long id: responseToRequest.getRequestIds()) {
                Request request = repository.findById(id).orElseThrow(NotFoundException::new);
                if (request.getStatus() != Status.PENDING) {
                    throw new RequestException("На заявку уже был отправлен ответ.");
                }
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                request.setStatus(Status.CONFIRMED);
                confirmedRequests.add(request);
            }
        }
        if (responseToRequest.getStatus().equals(Status.REJECTED)) {
            for (Long id: responseToRequest.getRequestIds()) {
                Request request = repository.findById(id).orElseThrow(NotFoundException::new);
                if (request.getStatus() != Status.PENDING) {
                    throw new RequestException("На заявку уже был отправлен ответ.");
                }
                request.setStatus(Status.REJECTED);
                rejectedRequests.add(request);
            }
        }
        eventService.saveEvent(event);
        repository.saveAll(confirmedRequests);
        repository.saveAll(rejectedRequests);
        ResponseToRequestResult result = new ResponseToRequestResult(
                RequestMapper.INSTANCE.collectionToRequestDto(confirmedRequests),
                RequestMapper.INSTANCE.collectionToRequestDto(rejectedRequests));
        return result;
    }
}
