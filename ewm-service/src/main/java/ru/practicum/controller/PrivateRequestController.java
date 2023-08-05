package ru.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateRequestController {
    private final RequestService service;

    public PrivateRequestController(RequestService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addNewRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return RequestMapper.INSTANCE.toRequestDto(service.createRequest(userId, eventId));
    }

    @GetMapping
    public List<RequestDto> getUsersRequests(@PathVariable Long userId) {
        return RequestMapper.INSTANCE.collectionToRequestDto(service.getUsersRequests(userId));
    }

    @PatchMapping("/{id}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long id) {
        return RequestMapper.INSTANCE.toRequestDto(service.cancelRequest(userId, id));
    }


}
