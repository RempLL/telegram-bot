package pro.sky.telegrambot.timer;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.entity.Task;
import pro.sky.telegrambot.repository.TaskRepository;
import pro.sky.telegrambot.service.TelegramBotService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class TaskTimer {
    private final TaskRepository taskRepository;
    private final TelegramBotService telegramBotService;

    public TaskTimer(TaskRepository taskRepository, TelegramBotService telegramBotService) {
        this.taskRepository = taskRepository;
        this.telegramBotService = telegramBotService;
    }

    @Scheduled(fixedDelay = 1_000L)
    public void task() {
        var not = taskRepository.findAllByDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        if (!not.isEmpty()) {
            for (Task notification : not) {
                telegramBotService.sendMessage(notification.getChatId(),
                        "Уведомление: " + notification.getText());
                taskRepository.delete(notification);
            }
        }
    }
}
