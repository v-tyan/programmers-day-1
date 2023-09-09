package ru.yandex.practicum.programmersdayTeam4.task1;

import ru.yandex.practicum.programmersdayTeam4.client.BaseClient;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class Task1 extends BaseClient {
    private static final String API_PREFIX_TASK1 = "/dev-day/register";
    private static final String SERVER_URL = "http://ya.praktikum.fvds.ru:8080";
    private static final Integer MAIN_ANSWER = 42;

    @Autowired
    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Autowired
    public Task1(RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(SERVER_URL + API_PREFIX_TASK1))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postTask1(String answer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("MAIN_ANSWER", String.valueOf(MAIN_ANSWER));

        HttpEntity<Object> requestEntity = new HttpEntity<>(answer, headers);

        ResponseEntity<Object> serverResponse;
        try {
            serverResponse = rest.exchange("", HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(serverResponse.getStatusCode());

        if (serverResponse.hasBody()) {
            return responseBuilder.body(serverResponse.getBody());
        }

        return responseBuilder.build();
    }

}