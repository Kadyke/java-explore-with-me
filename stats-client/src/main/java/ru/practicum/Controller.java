package ru.practicum;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public ResponseEntity<Object> getStats(@RequestParam(name = "end")
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                           @RequestParam(name = "start")
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        Map<String, Object> parameters = new HashMap<>(Map.of("end",
                end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "start", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "unique", unique));
        return client.getStats(parameters, uris);
    }
}
