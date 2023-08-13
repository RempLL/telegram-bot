package pro.sky.telegrambot.entity;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "chat_id")
    private long chatId;
    @Column(length = 4096)
    private String text;
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public Task(String text, long chatId, LocalDateTime dateTime) {
        this.text = text;
        this.chatId = chatId;
        this.dateTime = dateTime;
    }

    public Task() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
