package ru.yandex.practicum.programmersdayTeam4.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.yandex.practicum.programmersdayTeam4.client.BaseClient;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.yandex.practicum.programmersdayTeam4.model.Answer2;
import ru.yandex.practicum.programmersdayTeam4.model.Answer3;
import ru.yandex.practicum.programmersdayTeam4.model.In2;
import ru.yandex.practicum.programmersdayTeam4.model.In3;

@Service
public class Task extends BaseClient {
    private static final String API_PREFIX_TASK1 = "/dev-day/register";
    private static final String API_PREFIX_TASK2 = "/dev-day/task/2";
    private static final String API_PREFIX_TASK3 = "/dev-day/task/3";
    private static final String SERVER_URL = "http://ya.praktikum.fvds.ru:8080";
    private static final Integer MAIN_ANSWER = 42;
    private static final String TOKEN = "9b5e46cb-22f9-4e62-8277-e6fe02c96faf";

    private final Gson gson = getGson();

    @Autowired
    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Autowired
    public Task(RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(SERVER_URL))
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
            serverResponse = rest.exchange(API_PREFIX_TASK1, HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(serverResponse.getStatusCode());

        if (serverResponse.hasBody()) {
            return responseBuilder.body(serverResponse.getBody());
        }

        return responseBuilder.build();
    }

    public ResponseEntity<Object> postTask2() {
        String encoded = "";
        int offset = 0;

        Connection connection = Jsoup.connect(SERVER_URL + API_PREFIX_TASK2);
        connection.header("AUTH_TOKEN", TOKEN);

        try {
            Document docCustomConn = connection.get();
            Element text = docCustomConn.getElementsByTag("code").first().getElementsByTag("span").first();
            String inString = text.textNodes().get(0).toString();

            In2 in2 = gson.fromJson(inString, In2.class);


            encoded = in2.getEncoded();
            offset = in2.getOffset();
        } catch (IOException e) {

        }

        String answerString = decode(encoded, offset);

        Answer2 answer = new Answer2();
        answer.setDecoded(answerString);

        // ответ

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("AUTH_TOKEN", TOKEN);

        HttpEntity<Object> requestEntity = new HttpEntity<>(answer, headers);

        ResponseEntity<Object> serverResponse;
        try {
            serverResponse = rest.exchange(API_PREFIX_TASK2, HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(serverResponse.getStatusCode());

        if (serverResponse.hasBody()) {
            return responseBuilder.body(serverResponse.getBody());
        }

        return responseBuilder.build();
    }

    public ResponseEntity<Object> postTask3() {
        long left = 0;
        long right = Long.parseLong("FFFFFFFF", 16);
        long mid;
        String answer = "";
        In3 in3 = new In3();
        Answer3 answer3 = new Answer3();
        while (true) {
            if (left >= right) {
                break;
            }

            mid = left + ((right - left + 1) / 2);
            answer = Long.toString(mid, 16).toUpperCase();
            answer3.setPassword(answer);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.set("AUTH_TOKEN", TOKEN);

            HttpEntity<Object> requestEntity = new HttpEntity<>(answer3, headers);

            ResponseEntity<Object> serverResponse;
            try {
                serverResponse = rest.exchange(API_PREFIX_TASK3, HttpMethod.POST, requestEntity, Object.class);
                ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(serverResponse.getStatusCode());

                responseBuilder.body(serverResponse.getBody());
                return responseBuilder.build();
            } catch (HttpStatusCodeException e) {
                in3 = gson.fromJson(e.getMessage().toString(), In3.class);
                if (in3.getPrompt().equals("<pass")) {
                    right = mid;
                } else if (in3.getPrompt().equals(">pass")) {
                    left = mid;
                }

            }
        }
        return ResponseEntity.of(Optional.of(in3));
    }


    private String decode(String message, int offset) {
        //message = "VOJS O DSFTSQH DVD QCRWBU ROM";
        //offset = 14;
        offset = 26 - (offset % 26);
        StringBuilder result = new StringBuilder();
        for (char character : message.toCharArray()) {
            if (character != ' ') {
                int originalAlphabetPosition = character - 'A';
                int newAlphabetPosition = (originalAlphabetPosition + offset) % 26;
                char newCharacter = (char) ('A' + newAlphabetPosition);
                result.append(newCharacter);
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    private static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

}