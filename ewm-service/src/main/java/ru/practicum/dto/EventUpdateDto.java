package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.Location;
import ru.practicum.model.StateAction;
import ru.practicum.validation.EventDateAndMayBeNull;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateDto {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @EventDateAndMayBeNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @Size(min = 20, max = 7000)
    private String description;
    private Location location;
    private Integer participantLimit;
    private Boolean requestModeration;
    private Boolean paid;
    private StateAction stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
