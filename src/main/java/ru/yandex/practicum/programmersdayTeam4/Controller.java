package ru.yandex.practicum.programmersdayTeam4;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.programmersdayTeam4.task1.Task;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("http://ya.praktikum.fvds.ru:8080/dev-day/task/2")
public class Controller {

    private final Task task1;

    @GetMapping //("http://ya.praktikum.fvds.ru:8080/dev-day/task/2")
    public ResponseEntity<Object> getTask2(
            @RequestHeader("AUTH_TOKEN") Integer token) {

       // return task1.getTask2();
        return null;
    }

}
