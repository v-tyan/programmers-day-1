package ru.yandex.practicum.programmersdayTeam4;

import ru.yandex.practicum.programmersdayTeam4.task.Task;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ProgrammersDayTeam4Application {

    public static void main(String[] args) {
        SpringApplication.run(ProgrammersDayTeam4Application.class, args);
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();

        //makeTask1(context);
        //makeTask2(context);
        //makeTask3(context);
    }

    private static void makeTask1(ApplicationContext context) {
        Task task1 = context.getBean(Task.class);
        System.out.println(task1.postTask1(""));
    }

    private static void makeTask2(ApplicationContext context) {
        Task task1 = context.getBean(Task.class);
        System.out.println(task1.postTask2());
    }

    private static void makeTask3(ApplicationContext context) {
        Task task1 = context.getBean(Task.class);
        System.out.println(task1.postTask3());
    }

}