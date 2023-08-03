package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.Location;
import ru.practicum.validation.EventDateAndNotNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventInputDto {
    @NotBlank
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    private String description;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    @NotNull
    private Integer participantLimit;
    @EventDateAndNotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private Boolean requestModeration;
    @NotNull
    private String title;
}
