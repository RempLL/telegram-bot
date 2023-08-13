package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.TaskService;
import pro.sky.telegrambot.service.TelegramBotService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final Pattern PATTERN = Pattern.compile("(\\d{1,2}\\.\\d{1,2}\\.\\d{4} \\d{1,2}:\\d{2}) ([А-яA-z\\d,\\s.,@!:?]+)");

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
    private final TelegramBot telegramBot;

    private final TaskService taskService;
    private final TelegramBotService telegramBotService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, TaskService taskService, TelegramBotService telegramBotService) {
        this.telegramBot = telegramBot;
        this.taskService = taskService;
        this.telegramBotService = telegramBotService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Long id = update.message().chat().id();
            Message message = update.message();
            String text = message.text();
            LocalDateTime dateTime;
            if (update.message() != null && text != null) {
                Matcher matcher = PATTERN.matcher(text);
                if (text.equals("/start")) {
                    telegramBotService.sendMessage(id, "Привет " + update.message().from().firstName()
                            + ". Для планирования задачи отправьте ее в формате: \\01.01.2022 22:00 текст...");
                } else if (matcher.matches() && (dateTime=parse(matcher.group(1))) !=null) {
                    taskService.save(matcher.group(2), id, dateTime);
                    telegramBotService.sendMessage(id, update.message().from().firstName() + ", задача запланирована");
                } else {
                    telegramBotService.sendMessage(id, "Формат сообщения неверный");
                }
            } else {
                telegramBotService.sendMessage(id, update.message().from().firstName() + ", отправьте команду /start или запланируйте задачу");
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Nullable
    private LocalDateTime parse(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
