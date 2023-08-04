package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.yandex.Client;

@Configuration
public class ConfigurationBeans {

    @Bean
    public Client client(@Value("${service.url}") String serviceUrl) {;
        return new Client(serviceUrl);
    }
}
