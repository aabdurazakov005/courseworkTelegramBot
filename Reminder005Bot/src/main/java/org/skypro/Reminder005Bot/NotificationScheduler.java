package org.skypro.Reminder005Bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class NotificationScheduler {

    private final TelegramBot telegramBot;
    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationScheduler(@Value("${bot.token}") String token,
                                 NotificationTaskRepository notificationTaskRepository) {
        this.telegramBot = new TelegramBot(token);
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @Scheduled(cron = "0 * * * * *") // Каждую минуту в 00 секунд
    public void sendScheduledNotifications() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        List<NotificationTask> tasks = notificationTaskRepository.findByNotificationDateTime(currentTime);

        for (NotificationTask task : tasks) {
            String notificationText = String.format(
                    "🔔 Напоминание!\n\n%s",
                    task.getMessageText()
            );

            SendMessage message = new SendMessage(task.getChatId(), notificationText);
            SendResponse response = telegramBot.execute(message);

            if (response.isOk()) {
                notificationTaskRepository.delete(task);
            }
        }
    }
}