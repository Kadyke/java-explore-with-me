package ru.practicum.yandex;

import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class Client {
    private final RestTemplate rest;

    public Client(RestTemplate restTemplate) {
        this.rest = restTemplate;
    }

    public ResponseEntity<Object> saveHit(HitDto hitDto) {
        return makeAndSendRequest(HttpMethod.POST, "/hit", hitDto, null);
    }

    public ResponseEntity<Object> getStats(Map<String, Object> parameters, List<String> uris) {
        String path = "/stats?end={end}&start={start}&unique={unique}";
        if (uris != null) {
            Integer index = 0;
            for (String uri: uris) {
                path = path + "&uris={uris" + index + "}";
                parameters.put("uris" + index, uri);
                index++;
            }
        }
        return makeAndSendRequest(HttpMethod.GET, path, null, parameters);
    }

    private ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Object body,
                                                      Map<String, Object> parameters) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(body, defaultHeaders());
        ResponseEntity<Object> statsServerResponse;
        try {
            if (parameters != null) {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareResponse(statsServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }
}
