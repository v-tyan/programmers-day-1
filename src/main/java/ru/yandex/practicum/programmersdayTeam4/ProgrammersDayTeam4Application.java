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
        makeTask4(context);
    }

    private static void makeTask1(ApplicationContext context) {
        Task task = context.getBean(Task.class);
        System.out.println(task.postTask1(""));
    }

    private static void makeTask2(ApplicationContext context) {
        Task task = context.getBean(Task.class);
        System.out.println(task.postTask2());
    }

    private static void makeTask3(ApplicationContext context) {
        Task task = context.getBean(Task.class);
        System.out.println(task.postTask3());
    }

    private static void makeTask4(ApplicationContext context) {
        Task task = context.getBean(Task.class);
        System.out.println(task.postTask4());
    }

}