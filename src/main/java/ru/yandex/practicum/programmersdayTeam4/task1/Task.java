package ru.yandex.practicum.programmersdayTeam4.task1;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.yandex.practicum.programmersdayTeam4.client.BaseClient;

import java.io.IOError;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.yandex.practicum.programmersdayTeam4.model.Answer2;

@Service
public class Task extends BaseClient {
    private static final String API_PREFIX_TASK1 = "/dev-day/register";
    private static final String API_PREFIX_TASK2 = "/dev-day/task/2";
    private static final String SERVER_URL = "http://ya.praktikum.fvds.ru:8080";
    private static final Integer MAIN_ANSWER = 42;
    private static final String TOKEN = "9b5e46cb-22f9-4e62-8277-e6fe02c96faf";

    @Autowired
    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Autowired
    public Task(RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(SERVER_URL + API_PREFIX_TASK2))
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

    public ResponseEntity<Object> getTask2() {
        String encoded = "";
        int offset = 0;

       /* HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.TEXT_HTML);
        headers1.set("AUTH_TOKEN", String.valueOf(TOKEN));
        HttpEntity<Object> requestEntity1 = new HttpEntity<>("", headers1);

        ResponseEntity<String> serverResponse1;
        try {
            try {
                serverResponse1 = rest.exchange("", HttpMethod.GET, requestEntity1, String.class);
            } catch (HttpStatusCodeException e) {
                return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
*/




        Connection connection = Jsoup.connect("http://ya.praktikum.fvds.ru:8080/dev-day/task/2");
        connection.header("AUTH_TOKEN", TOKEN);

        try {
            Document docCustomConn = connection.get();
            Element text = docCustomConn.getElementById("message");

            ////// todo
            encoded = text.getElementsByAttribute("encoded").toString();
            offset = Integer.parseInt(text.getElementsByAttribute("offset").toString());
        } catch (IOException e) {

        }

        String answerString = decode(encoded, offset);

        Answer2 answer = new Answer2();
        answer.setDecoded(answerString);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("AUTH_TOKEN", TOKEN);

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

}