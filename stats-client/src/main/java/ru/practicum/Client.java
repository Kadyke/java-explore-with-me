package ru.practicum;

import io.micrometer.core.lang.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Component
public class Client {
    private final RestTemplate rest;

    public Client(@Value("${service.url}") String serviceUrl) {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        this.rest = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build();
    }

    public ResponseEntity<Object> saveHit(HitDto hitDto) {
        return makeAndSendRequest(HttpMethod.POST, "/hit", hitDto);
    }

    public ResponseEntity<Object> getStats() {
        return makeAndSendRequest(HttpMethod.GET, "/stats", null);
    }

    private ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable Object body) {
        HttpEntity<Object> requestEntity;
        if (body == null) {
            requestEntity = new HttpEntity<>(null);
        } else {
            requestEntity = new HttpEntity<>(body);
        }
        ResponseEntity<Object> response;
        try {
            response = rest.exchange(path, method, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(response);
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
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
