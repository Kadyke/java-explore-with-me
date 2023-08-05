package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseToRequestResult {
    List<RequestDto> confirmedRequests;
    List<RequestDto> rejectedRequests;
}
