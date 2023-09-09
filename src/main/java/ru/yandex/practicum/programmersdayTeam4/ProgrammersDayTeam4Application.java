package ru.yandex.practicum.programmersdayTeam4;

import ru.yandex.practicum.programmersdayTeam4.task1.Task1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ProgrammersDayTeam4Application {

    public static void main(String[] args) {
        SpringApplication.run(ProgrammersDayTeam4Application.class, args);
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();

        //makeTask1(context);
    }

    private static void makeTask1(ApplicationContext context) {
        Task1 task1 = context.getBean(Task1.class);
        System.out.println(task1.postTask1(""));
    }

}