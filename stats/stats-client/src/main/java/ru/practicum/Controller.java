package ru.practicum;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping
public class Controller {
    private final Client client;

    public Controller(Client client) {
        this.client = client;
    }

    @PostMapping("/hit")
    public HttpStatus saveHit(@RequestBody @Valid HitDto hitDto) {
        return client.saveHit(hitDto).getStatusCode();
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats() {
        return client.getStats();
    }
}
