package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.yandex.Client;

@Configuration
public class ConfigurationBeans {

    @Bean
    public Client client(@Value("${service.url}") String serviceUrl) {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate rest = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build();
        return new Client(rest);
    }
}
