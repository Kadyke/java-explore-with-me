package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationInputDto {
    private Set<Long> events;
    @NotNull
    private Boolean pinned;
    @NotBlank
    private String title;
}
