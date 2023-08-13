package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.entity.Task;
import pro.sky.telegrambot.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void save(String text, long chatId, LocalDateTime dateTime) {
        taskRepository.save(new Task(text, chatId, dateTime.truncatedTo(ChronoUnit.MINUTES)));
    }

}
