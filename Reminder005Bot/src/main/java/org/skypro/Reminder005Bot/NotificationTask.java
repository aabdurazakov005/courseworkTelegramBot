package org.skypro.Reminder005Bot;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_task")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "message_text", nullable = false)
    private String messageText;

    @Column(name = "notification_date_time", nullable = false)
    private LocalDateTime notificationDateTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Конструкторы
    public NotificationTask() {}

    public NotificationTask(Long chatId, String messageText, LocalDateTime notificationDateTime) {
        this.chatId = chatId;
        this.messageText = messageText;
        this.notificationDateTime = notificationDateTime;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }

    public LocalDateTime getNotificationDateTime() { return notificationDateTime; }
    public void setNotificationDateTime(LocalDateTime notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}