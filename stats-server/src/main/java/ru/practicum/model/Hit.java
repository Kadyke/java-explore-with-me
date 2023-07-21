package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hit {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private Timestamp timestamp;
}
