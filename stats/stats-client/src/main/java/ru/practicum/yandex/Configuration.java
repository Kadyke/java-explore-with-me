package ru.practicum.yandex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

public class Configuration {

    public RestTemplate restTemplate(@Value("${service.url}") String serviceUrl) {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return builder.uriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build();
    }

}
