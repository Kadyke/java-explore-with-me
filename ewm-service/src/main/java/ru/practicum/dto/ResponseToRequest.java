package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.Status;
import ru.practicum.validation.IsNotPending;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseToRequest {
    @NotEmpty
    private List<Long> requestIds;
    @IsNotPending
    private Status status;
}
