package ru.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserDto;
import ru.practicum.mapper.UserMapper;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
public class AdminUserController {
    private final UserService service;

    public AdminUserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addNewUser(@RequestBody @Valid UserDto userDto) {
        return UserMapper.INSTANCE.toUserDto(service.createUser(UserMapper.INSTANCE.toUser(userDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public HttpStatus deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return HttpStatus.NO_CONTENT;
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size,
                                  @RequestParam(required = false) List<Long> ids) {
        if (ids == null) {
            return UserMapper.INSTANCE.collectionToUserDto(service.getUsers(from, size));
        }
        return UserMapper.INSTANCE.collectionToUserDto(service.getUsers(from, size, ids));
    }
}
